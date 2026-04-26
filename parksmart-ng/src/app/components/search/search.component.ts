import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { SearchService, ParkingCard, SpaceSearchRequest, Suggestion } from '../../services/search.service';
import is from '@angular/common/locales/extra/is';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  private locationSubject = new Subject<string>();

  location = '';
  userLat?: number;
  userLng?: number;
  arriving = '';
  leaving = '';
  loading = false;
  cards: ParkingCard[] = [];
  error = '';

  // Filters
  typePublic = true;
  typePrivate = true;
  minPrice: number | null = null;
  maxPrice: number | null = null;
  amenityEV = false;
  amenityCovered = false;
  amenityCCTV = false;
  amenityGuarded = false;
  radius = 10;
  sortBy = '';

  // Location suggestions
  suggestions: Suggestion[] = [];
  showSuggestions = false;

  // Expanded card for map
  expandedCardId: number | null = null;

  constructor(private searchSvc: SearchService, private router: Router) {}

  ngOnInit() {
    this.loading = true;
   const now = new Date();
  const arrivalTime = new Date(now.getTime() + 16 * 60 * 1000);
  const leavingTime = new Date(arrivalTime.getTime() + 60 * 60 * 1000);

    this.arriving = this.toLocal(arrivalTime);
    this.leaving = this.toLocal(leavingTime);

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        pos => {
          this.userLat = pos.coords.latitude;
          this.userLng = pos.coords.longitude;
          this.fetchLocationName(pos.coords.latitude, pos.coords.longitude);
        },
        () => {
          this.loading = false;
          this.error = 'Unable to determine location. Please enable location services.';
        }
      );
    } else {
      this.loading = false;
      this.error = 'Geolocation is not supported by your browser.';
    }

    // Location input debounce
    this.locationSubject.pipe(
      debounceTime(250),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(q => {
      if (q.length >= 2) {
        this.searchSvc.suggest(q).subscribe({
          next: s => { this.suggestions = s; this.showSuggestions = s.length > 0; },
          error: () => {}
        });
      } else {
        this.suggestions = [];
        this.showSuggestions = false;
      }
    });
  }

  ngOnDestroy() { this.destroy$.next(); this.destroy$.complete(); }

  private fetchLocationName(lat: number, lng: number) {
    fetch(`https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json`)
      .then(r => r.json())
      .then(data => {
        const addr = data.address;
        this.location = addr.suburb || addr.city_district || addr.neighbourhood || addr.city || 'Current Location';
        this.doSearch();
      })
      .catch(() => this.doSearch());
  }

  toLocal(d: Date): string {
    const p = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${p(d.getMonth()+1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
  }

  onLocationInput() { this.locationSubject.next(this.location); }
  selectSuggestion(s: Suggestion) { this.location = s.label; this.userLat = s.latitude; this.userLng = s.longitude; this.showSuggestions = false; this.doSearch(); }
  hideSuggestions() { setTimeout(() => this.showSuggestions = false, 150); }

  buildRequest(): SpaceSearchRequest {
    const types: string[] = [];
    if (this.typePublic) types.push('PUBLIC');
    if (this.typePrivate) types.push('PRIVATE');
    const amenities: string[] = [];
    if (this.amenityEV) amenities.push('EV');
    if (this.amenityCovered) amenities.push('COVERED');
    if (this.amenityCCTV) amenities.push('CCTV');
    if (this.amenityGuarded) amenities.push('GUARDED');
    return {
      latitude: this.userLat!,
      longitude: this.userLng!,
      arrival: this.arriving || undefined,
      leaving: this.leaving || undefined,
      types: types.length ? types : undefined,
      minPrice: this.minPrice ?? undefined,
      maxPrice: this.maxPrice ?? undefined,
      amenities: amenities.length ? amenities : undefined,
      radius: this.radius,
      sortBy: this.sortBy || undefined,
      page: 1,
      limit: 50
    };
  }

  doSearch() {
    if (this.userLat == null || this.userLng == null) {
      this.loading = false;
      this.error = 'Waiting for location data. Please allow location access and try again.';
      return;
    }

    this.loading = true;
    this.error = '';
    this.expandedCardId = null;
    this.searchSvc.search(this.buildRequest()).subscribe({
      next: cards => { this.cards = cards; this.loading = false; },
      error: err => { this.error = err?.error || 'Search failed'; this.loading = false; }
    });
  }

  onFilterChange() { this.doSearch(); }

  toggleCard(id: number) {
    this.expandedCardId = this.expandedCardId === id ? null : id;
  }

  getMapUrl(card: ParkingCard): string {
    return `https://maps.google.com/maps?q=${card.latitude},${card.longitude}&z=16&output=embed`;
  }

  reserve(card: ParkingCard) {
    this.router.navigate(['/slots', card.spaceId], {
      state: { card, arriving: this.arriving, leaving: this.leaving }
    });
  }

  stars(rating: number): number[] { return Array(5).fill(0).map((_, i) => i < Math.round(rating) ? 1 : 0); }

  clearFilters() {
    this.typePublic = true; this.typePrivate = true;
    this.minPrice = null; this.maxPrice = null;
    this.amenityEV = false; this.amenityCovered = false;
    this.amenityCCTV = false; this.amenityGuarded = false;
    this.radius = 10; this.sortBy = '';
    this.doSearch();
  }
}
