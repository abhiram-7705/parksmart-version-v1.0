import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardService, BookingCard } from '../../services/dashboard.service';
import { OwnerService } from '../../services/owner.service';
import { BookingService } from '../../services/booking.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  activeTab: 'user' | 'owner' = 'user';
  // User dashboard
  bookings: BookingCard[] = [];
  filteredBookings: BookingCard[] = [];
  searchQuery = '';
  searchSuggestions: string[] = [];
  showSearchSuggestions = false;
  statusFilter: string[] = [];
  typeFilter: string[] = [];
  sortBy = '';
  bookingsLoading = true;
  // Owner dashboard
  spaces: any[] = [];
  spacesLoading = false;
  // Modals
  showDirections: BookingCard | null = null;
  showContact: BookingCard | null = null;
  showReceipt: any = null;
  showRating: BookingCard | null = null;
  showQR: BookingCard | null = null;
  showCancelConfirm: BookingCard | null = null;
  showOwnerSlots: any = null;
  showOwnerBookings: any = null;
  showOwnerTimeline: any = null;
  showOwnerMore: any = null;
  showOwnerEdit: any = null;
  showAddSpace = false;
  showDeleteConfirm: any = null;
  ownerSlots: any[] = [];
  ownerBookings: any[] = [];
  ownerTimeline: any[] = [];
  ownerBookingFilter = 'ALL';
  timelineDate = new Date().toISOString().split('T')[0];
  receiptInvoice: any = null;
  ratingValue = 0;
  ratingComment = '';
  ratingSubmitting = false;
  contactForm = { spaceId: 0, name: '', email: '', phone: '', message: '', preferredContact: '', preferredTime: '' };
  contactSubmitting = false;
  contactSuccess = '';
  cancelLoading = false;
  userRole = 'USER';
  // Add/edit space
  spaceForm: any = { name: '', type: 'PUBLIC', pricePerHour: null, location: '', city: '', latitude: null, longitude: null, cctv: false, evCharging: false, guarded: false, coveredFence: false, notes: '' };
  spaceFormError = '';
  spaceFormLoading = false;
  editSpaceData: any = null;
  addSlotSpaceId: number | null = null;
  newSlotName = '';
  addSlotLoading = false;
  deleteSlotConfirm: any = null;
  showDeleteSlotConfirm = false;

  constructor(
    private dashSvc: DashboardService,
    private ownerSvc: OwnerService,
    private bookingSvc: BookingService,
    private userSvc: UserService,
    private router: Router
  ) {}

  ngOnInit() {
    this.userSvc.getProfile().subscribe(p => { this.userRole = p.role; });
    this.loadUserBookings();
  }

  // ── TAB ──
  switchTab(tab: 'user' | 'owner') {
    this.activeTab = tab;
    if (tab === 'owner' && this.spaces.length === 0) this.loadOwnerSpaces();
  }

  // ── USER DASHBOARD ──
  loadUserBookings() {
    this.bookingsLoading = true;
    this.dashSvc.searchBookings(this.searchQuery || undefined, this.statusFilter.length ? this.statusFilter : undefined,
      this.typeFilter.length ? this.typeFilter : undefined, this.sortBy || undefined).subscribe({
      next: b => { this.bookings = b; this.filteredBookings = b; this.bookingsLoading = false; },
      error: () => this.bookingsLoading = false
    });
  }

  applyFilters() { this.loadUserBookings(); }
  resetFilters() { this.searchQuery = ''; this.statusFilter = []; this.typeFilter = []; this.sortBy = ''; this.loadUserBookings(); }

  onSearchInput() {
    if (this.searchQuery.length >= 2) {
      this.dashSvc.getSuggestions(this.searchQuery).subscribe({
        next: s => { this.searchSuggestions = s; this.showSearchSuggestions = s.length > 0; },
        error: () => {}
      });
    } else { this.searchSuggestions = []; this.showSearchSuggestions = false; }
  }

  selectSuggestion(s: string) { this.searchQuery = s; this.showSearchSuggestions = false; }

  // Status counts from current filtered list
  countByStatus(status: string): number { return this.filteredBookings.filter(b => b.status === status).length; }

  toggleStatusFilter(s: string) {
    const i = this.statusFilter.indexOf(s);
    if (i >= 0) this.statusFilter.splice(i, 1); else this.statusFilter.push(s);
  }
  toggleTypeFilter(t: string) {
    const i = this.typeFilter.indexOf(t);
    if (i >= 0) this.typeFilter.splice(i, 1); else this.typeFilter.push(t);
  }

  formatDt(dt: string): string {
    if (!dt) return '';
    return new Date(dt).toLocaleString('en-IN', { dateStyle: 'short', timeStyle: 'short' });
  }

  // Receipt
  openReceipt(b: BookingCard) {
    this.receiptInvoice = null;
    this.showReceipt = b;
    this.bookingSvc.getInvoice([b.bookingId]).subscribe({ next: inv => this.receiptInvoice = inv, error: () => {} });
  }

  // Cancel
  cancelBooking() {
    if (!this.showCancelConfirm) return;
    this.cancelLoading = true;
    this.dashSvc.cancelBooking(this.showCancelConfirm.bookingId).subscribe({
      next: () => { this.showCancelConfirm = null; this.cancelLoading = false; this.loadUserBookings(); },
      error: () => { this.cancelLoading = false; }
    });
  }

  // Rating
  submitRating() {
    if (!this.showRating || this.ratingValue === 0) return;
    this.ratingSubmitting = true;
    this.dashSvc.submitRating(this.showRating.bookingId, this.ratingValue, this.ratingComment).subscribe({
      next: () => { this.showRating = null; this.ratingSubmitting = false; this.ratingValue = 0; this.ratingComment = ''; this.loadUserBookings(); },
      error: () => this.ratingSubmitting = false
    });
  }

  // Extend → go to slot selection
  extendBooking(b: BookingCard) {
    this.router.navigate(['/slots', 0], {
      state: { arriving: b.leaving, card: { name: b.spaceName, city: b.city, type: b.spaceType, spaceId: 0 } }
    });
  }

  // Contact
  openContact(b: BookingCard) {
    this.showContact = b;
    this.contactForm = { spaceId: b.bookingId, name: '', email: '', phone: '', message: '', preferredContact: '', preferredTime: 'Any Time' };
    this.contactSuccess = '';
  }
  submitContact() {
    this.contactSubmitting = true;
    this.dashSvc.submitContact(this.contactForm).subscribe({
      next: () => { this.contactSuccess = 'Request sent!'; this.contactSubmitting = false; },
      error: () => this.contactSubmitting = false
    });
  }

  // ── OWNER DASHBOARD ──
  loadOwnerSpaces() {
    this.spacesLoading = true;
    this.ownerSvc.getSpaces().subscribe({ next: s => { this.spaces = s; this.spacesLoading = false; }, error: () => this.spacesLoading = false });
  }

  openSlots(space: any) {
    this.showOwnerSlots = space;
    this.ownerSvc.getSlots(space.spaceId).subscribe({ next: s => this.ownerSlots = s, error: () => {} });
  }

  openBookings(space: any) {
    this.showOwnerBookings = space;
    this.ownerBookingFilter = 'ALL';
    this.loadOwnerBookings(space.spaceId);
  }

  loadOwnerBookings(spaceId: number) {
    this.ownerSvc.getBookings(spaceId, this.ownerBookingFilter).subscribe({ next: b => this.ownerBookings = b, error: () => {} });
  }

  openTimeline(space: any) {
    this.showOwnerTimeline = space;
    this.loadTimeline(space.spaceId);
  }

  loadTimeline(spaceId: number) {
    this.ownerSvc.getTimeline(spaceId, this.timelineDate).subscribe({ next: t => this.ownerTimeline = t, error: () => {} });
  }

  openMore(space: any) {
    this.showOwnerMore = null;
    this.ownerSvc.getSpaceDetail(space.spaceId).subscribe({ next: d => this.showOwnerMore = d, error: () => {} });
  }

  openEdit(space: any) {
    this.editSpaceData = space;
    this.spaceForm = { name: space.name, type: space.type, pricePerHour: space.pricePerHour,
      cctv: space.facilities?.includes('CCTV'), evCharging: space.facilities?.includes('EV'),
      guarded: space.facilities?.includes('Guarded'), coveredFence: space.facilities?.includes('Covered'), notes: '' };
    this.showOwnerEdit = true; this.spaceFormError = '';
  }

  saveEdit() {
    this.spaceFormLoading = true; this.spaceFormError = '';
    this.ownerSvc.editSpace({ ...this.spaceForm, spaceId: this.editSpaceData.spaceId }).subscribe({
      next: () => { this.spaceFormLoading = false; this.showOwnerEdit = false; this.loadOwnerSpaces(); },
      error: err => { this.spaceFormLoading = false; this.spaceFormError = err?.error || 'Failed to update'; }
    });
  }

  deleteSpace(space: any) {
    this.showDeleteConfirm = space;
  }

  confirmDeleteSpace() {
    this.ownerSvc.deleteSpace(this.showDeleteConfirm.spaceId).subscribe({
      next: () => { this.showDeleteConfirm = null; this.loadOwnerSpaces(); },
      error: err => { this.showDeleteConfirm = null; alert(err?.error || 'Cannot delete'); }
    });
  }

  toggleSlot(slot: any) {
    this.ownerSvc.toggleSlot(slot.slotId).subscribe({
      next: () => { if (this.showOwnerSlots) this.openSlots(this.showOwnerSlots); },
      error: () => {}
    });
  }

  confirmDeleteSlot(slot: any) { this.deleteSlotConfirm = slot; this.showDeleteSlotConfirm = true; }

  doDeleteSlot() {
    this.ownerSvc.deleteSlot(this.deleteSlotConfirm.slotId).subscribe({
      next: () => { this.showDeleteSlotConfirm = false; if (this.showOwnerSlots) this.openSlots(this.showOwnerSlots); },
      error: err => { this.showDeleteSlotConfirm = false; alert(err?.error || 'Cannot delete'); }
    });
  }

  addSlot() {
    if (!this.newSlotName || !this.showOwnerSlots) return;
    this.addSlotLoading = true;
    this.ownerSvc.addSlot(this.showOwnerSlots.spaceId, this.newSlotName.toUpperCase()).subscribe({
      next: () => { this.addSlotLoading = false; this.newSlotName = ''; this.openSlots(this.showOwnerSlots); },
      error: err => { this.addSlotLoading = false; alert(err?.error || 'Failed to add slot'); }
    });
  }

  useCurrentLocation() {
    if (!navigator.geolocation) return;
    navigator.geolocation.getCurrentPosition(pos => {
      const { latitude, longitude } = pos.coords;
      this.spaceForm.latitude = latitude; this.spaceForm.longitude = longitude;
      fetch(`https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`)
        .then(r => r.json()).then(data => {
          const addr = data.address;
          this.spaceForm.city = addr.city || addr.town || addr.village || '';
          this.spaceForm.location = addr.suburb || addr.neighbourhood || addr.city_district || this.spaceForm.city;
        }).catch(() => {});
    });
  }

  submitAddSpace() {
    this.spaceFormLoading = true; this.spaceFormError = '';
    this.ownerSvc.addSpace(this.spaceForm).subscribe({
      next: () => { this.spaceFormLoading = false; this.showAddSpace = false; this.loadOwnerSpaces(); this.spaceForm = { name:'', type:'PUBLIC', pricePerHour:null, location:'', city:'', latitude:null, longitude:null, cctv:false, evCharging:false, guarded:false, coveredFence:false, notes:'' }; },
      error: err => { this.spaceFormLoading = false; this.spaceFormError = err?.error || 'Failed to add space'; }
    });
  }

  getOwnerReceiptInvoice(b: any) {
    this.receiptInvoice = null;
    this.showReceipt = b;
    this.bookingSvc.getInvoice([b.bookingId]).subscribe({ next: inv => this.receiptInvoice = inv, error: () => {} });
  }

  stars5 = [1,2,3,4,5];
}
