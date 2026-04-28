import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ModalService } from '../../services/modal.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  searchQuery = '';
  showSuggestions = false;
  arriving = '';
  leaving = '';
  filteredLocations: string[] = [];

  private allLocations = [
    'T Nagar, Chennai', 'Marina Beach, Chennai', 'Adyar, Chennai',
    'Velachery, Chennai', 'Anna Nagar, Chennai', 'Nungambakkam, Chennai',
    'OMR, Chennai', 'Egmore, Chennai', 'Mylapore, Chennai', 'Porur, Chennai',
    'Tambaram, Chennai', 'Koyambedu, Chennai', 'Guindy, Chennai',
    'Chromepet, Chennai', 'Sholinganallur, Chennai', 'Thoraipakkam, Chennai',
    'Medavakkam, Chennai', 'Perambur, Chennai', 'Ambattur, Chennai',
    'Pallavaram, Chennai'
  ];

  constructor(private modal: ModalService, private auth: AuthService, private router: Router) {}

  ngOnInit() {
    const now = new Date();
    const later = new Date(now.getTime() + 2 * 3600000);
    this.arriving = this.toLocal(now);
    this.leaving = this.toLocal(later);
  }

  toLocal(d: Date): string {
    const p = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${p(d.getMonth()+1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
  }

  onInput() {
    const q = this.searchQuery.trim().toLowerCase();
    if (!q) { this.filteredLocations = []; this.showSuggestions = false; return; }
    this.filteredLocations = this.allLocations.filter(l => l.toLowerCase().includes(q)).slice(0, 7);
    this.showSuggestions = this.filteredLocations.length > 0;
  }

  selectLocation(loc: string) {
    this.searchQuery = loc;
    this.showSuggestions = false;
  }

  @HostListener('document:click', ['$event']) onDocClick(e: Event) {
    if (!(e.target as HTMLElement).closest('.search-wrap')) this.showSuggestions = false;
  }

  triggerLogin() {
    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/search']);
    } else {
      this.modal.openLogin();
    }
  }
}
