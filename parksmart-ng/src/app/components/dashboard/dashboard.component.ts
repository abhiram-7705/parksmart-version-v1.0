import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardService, BookingCard, ContactReq } from '../../services/dashboard.service';
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
  maxTimelineDate = new Date(Date.now() + 90 * 86400000).toISOString().split('T')[0];
  timelineHours: number[] = Array.from({ length: 24 }, (_, i) => i);

  formatTlHour(h: number): string {
    const ampm = h >= 12 ? 'PM' : 'AM';
    const d = h % 12 || 12;
    return `${d}${ampm}`;
  }

  getBlockLeft(arrival: string): number {
    const d = new Date(arrival);
    const mins = d.getHours() * 60 + d.getMinutes();
    return (mins / 1440) * 100;
  }

  getBlockWidth(arrival: string, leaving: string): number {
    const a = new Date(arrival);
    const l = new Date(leaving);
    const aMins = a.getHours() * 60 + a.getMinutes();
    let lMins = l.getHours() * 60 + l.getMinutes();
    // If leaving is next day, cap at 1440
    if (l.getDate() !== a.getDate()) lMins = 1440;
    const w = ((lMins - aMins) / 1440) * 100;
    return Math.max(w, 0.5);
  }
  receiptInvoice: any = null;
  ratingValue = 0;
  ratingComment = '';
  ratingSubmitting = false;
  contactForm = { spaceId: 0, name: '', email: '', phone: '', message: '', preferredContact: '', preferredTime: '' };
  contactSubmitting = false;
  contactSuccess = '';
  contactError = '';
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
  addSlotError = '';
  contactFormErrors: Record<string, string> = {};
  ratedBookingIds = new Set<number>();
  showOwnerReceipt = false;

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
    this.loadUnreadContactCount();
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
  resetFilters() { this.searchQuery = ''; this.statusFilter = []; this.typeFilter = []; this.statusFilterSingle = ''; this.typeFilterSingle = ''; this.sortBy = ''; this.loadUserBookings(); }

  private searchDebounce: any;

  onSearchInput() {
    if (this.searchQuery.length >= 2) {
      this.dashSvc.getSuggestions(this.searchQuery).subscribe({
        next: s => { this.searchSuggestions = s; this.showSearchSuggestions = s.length > 0; },
        error: () => {}
      });
    } else { this.searchSuggestions = []; this.showSearchSuggestions = false; }
    // Debounce search
    clearTimeout(this.searchDebounce);
    this.searchDebounce = setTimeout(() => this.loadUserBookings(), 350);
  }

  selectSuggestion(s: string) { this.searchQuery = s; this.showSearchSuggestions = false; this.loadUserBookings(); }

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
      next: () => {
        this.ratedBookingIds.add(this.showRating!.bookingId);
        this.showRating = null; this.ratingSubmitting = false; this.ratingValue = 0; this.ratingComment = ''; this.loadUserBookings();
      },
      error: (err) => {
        if (err?.error?.includes('already rated')) this.ratedBookingIds.add(this.showRating!.bookingId);
        this.ratingSubmitting = false;
      }
    });
  }

  statusFilterSingle = '';
  typeFilterSingle = '';

  onStatusDropdownChange() {
    this.statusFilter = this.statusFilterSingle ? [this.statusFilterSingle] : [];
    this.loadUserBookings();
  }

  onTypeDropdownChange() {
    this.typeFilter = this.typeFilterSingle ? [this.typeFilterSingle] : [];
    this.loadUserBookings();
  }

  // Extend → go to slot selection for same space with arrival = booking's leaving
  extendBooking(b: BookingCard) {
    this.router.navigate(['/slots', b.spaceId ?? 0], {
      state: {
        arriving: b.leaving,
        leaving: new Date(new Date(b.leaving).getTime() + 3600000).toISOString().slice(0, 16),
        card: { name: b.spaceName, city: b.city, type: b.spaceType, spaceId: b.spaceId ?? 0 }
      }
    });
  }

  // Contact
  openContact(b: BookingCard) {
    this.showContact = b;
    this.contactForm = { spaceId: b.spaceId ?? 0, name: '', email: '', phone: '', message: '', preferredContact: '', preferredTime: '' };
    this.contactSuccess = '';
    this.contactError = '';
    this.contactFormErrors = {};
  }

  validateContactForm(): string {
    this.contactFormErrors = {};
    if (!this.contactForm.name.trim()) this.contactFormErrors['name'] = 'Name is required';
    if (!this.contactForm.email.trim()) this.contactFormErrors['email'] = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.contactForm.email)) this.contactFormErrors['email'] = 'Invalid email address';
    if (!this.contactForm.phone.trim()) this.contactFormErrors['phone'] = 'Phone is required';
    else if (!/^[0-9]{7,15}$/.test(this.contactForm.phone.replace(/\D/g, ''))) this.contactFormErrors['phone'] = 'Invalid phone number';
    if (!this.contactForm.message.trim()) this.contactFormErrors['message'] = 'Message is required';
    return Object.keys(this.contactFormErrors).length > 0 ? 'errors' : '';
  }

  submitContact() {
    const err = this.validateContactForm();
    if (err) return;
    this.contactError = '';
    this.contactSubmitting = true;
    this.dashSvc.submitContact({ ...this.contactForm, phone: Number(this.contactForm.phone) }).subscribe({
      next: () => {
        this.contactSubmitting = false;
        this.contactSuccess = 'Request sent!';
        setTimeout(() => { this.showContact = null; this.contactSuccess = ''; }, 1500);
      },
      error: (e) => { this.contactSubmitting = false; this.contactError = e?.error || 'Failed to submit'; }
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
    // Validate same rules as add space
    if (!this.spaceForm.name || !this.spaceForm.name.trim()) {
      this.spaceFormError = 'Name is required';
      return;
    }
    const nameRegex = /^[a-zA-Z\s]+$/;
    if (this.spaceForm.name.trim().length < 5 || !nameRegex.test(this.spaceForm.name.trim())) {
      this.spaceFormError = 'Name must be at least 5 characters and contain only alphabets';
      return;
    }
    if (this.spaceForm.pricePerHour === null || this.spaceForm.pricePerHour === undefined || this.spaceForm.pricePerHour === '') {
      this.spaceFormError = 'Price per hour is required';
      return;
    }
    if (this.spaceForm.pricePerHour <= 0) {
      this.spaceFormError = 'Price per hour must be greater than 0';
      return;
    }
    this.spaceFormLoading = true; this.spaceFormError = '';
    this.ownerSvc.editSpace({ ...this.spaceForm, spaceId: this.editSpaceData.spaceId }).subscribe({
      next: () => { this.spaceFormLoading = false; this.showOwnerEdit = false; this.loadOwnerSpaces(); },
      error: err => { this.spaceFormLoading = false; this.spaceFormError = err?.error || 'Failed to update'; }
    });
  }

  deleteSpaceError = '';

  deleteSpace(space: any) {
    this.showDeleteConfirm = space;
    this.deleteSpaceError = '';
  }

  confirmDeleteSpace() {
    this.ownerSvc.deleteSpace(this.showDeleteConfirm.spaceId).subscribe({
      next: () => { this.showDeleteConfirm = null; this.deleteSpaceError = ''; this.loadOwnerSpaces(); },
      error: err => { this.deleteSpaceError = err?.error || 'Cannot delete space with active bookings'; }
    });
  }

  toggleSlot(slot: any) {
    this.ownerSvc.toggleSlot(slot.slotId).subscribe({
      next: () => { if (this.showOwnerSlots) this.openSlots(this.showOwnerSlots); },
      error: () => {}
    });
  }

  deleteSlotError = '';

  // Approval requests
  ownerApprovals: any[] = [];
  approvalsOpen = false;

  // Contact requests (owner inbox)
  ownerContacts: any[] = [];
  unreadContactCount = 0;
  showOwnerContacts = false;
  contactsLoading = false;

  // Public slot generator
  showGenerateSlots: any = null;
  genLevels: number | null = null;
  genSlotsPerLevel: number | null = null;
  genLoading = false;
  genError = '';
  genSuccess = '';

  confirmDeleteSlot(slot: any) { this.showDeleteConfirm = slot; this.deleteSlotError = ''; }

  doDeleteSlot() {
    this.ownerSvc.deleteSlot(this.showDeleteConfirm.slotId).subscribe({
      next: () => { this.showDeleteConfirm = null; this.deleteSlotError = ''; if (this.showOwnerSlots) this.openSlots(this.showOwnerSlots); },
      error: err => { this.deleteSlotError = err?.error || 'Cannot delete slot with active bookings'; }
    });
  }

  addSlot() {
    if (!this.newSlotName || !this.showOwnerSlots) return;
    
    // Validate: 1-2 alphabets followed by numbers
    const slotNameRegex = /^[a-zA-Z]{1,2}[0-9]+$/;
    if (!slotNameRegex.test(this.newSlotName.trim())) {
      this.addSlotError = 'Slot name must have 1-2 alphabets followed by numbers (e.g., A1, B12, P100)';
      return;
    }
    
    this.addSlotLoading = true;
    this.addSlotError = '';
    this.ownerSvc.addSlot(this.showOwnerSlots.spaceId, this.newSlotName.toUpperCase()).subscribe({
      next: () => { this.addSlotLoading = false; this.newSlotName = ''; this.addSlotError = ''; this.openSlots(this.showOwnerSlots); },
      error: err => { this.addSlotLoading = false; this.addSlotError = err?.error || 'Failed to add slot'; }
    });
  }

useCurrentLocation() {
  if (!navigator.geolocation) {
    alert("Geolocation not supported");
    return;
  }

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const { latitude, longitude } = pos.coords;

      this.spaceForm.latitude = latitude;
      this.spaceForm.longitude = longitude;

      fetch(`https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`)
        .then(r => r.json())
        .then(data => {
          console.log("Nominatim response:", data); // 🔍 DEBUG

          const addr = data.address || {};

          // ✅ Stronger fallback chain
          this.spaceForm.city =
            addr.city ||
            addr.town ||
            addr.village ||
            addr.state_district ||
            addr.county ||
            "Chennai"; // fallback for demo

          this.spaceForm.location =
            addr.suburb ||
            addr.neighbourhood ||
            addr.city_district ||
            addr.road ||
            this.spaceForm.city;

          // 🔥 FORCE Angular to detect change
          this.spaceForm = { ...this.spaceForm };
        })
        .catch(err => {
          console.error("Reverse geocoding failed:", err);

          // fallback for demo
          this.spaceForm.city = "Chennai";
          this.spaceForm.location = `Lat: ${latitude}, Lng: ${longitude}`;
        });
    },
    (err) => {
      console.error("Geolocation error:", err);
      alert("Unable to fetch location");
    }
  );
}

  submitAddSpace() {
    // Validate name: at least 5 characters and only alphabets
    if (!this.spaceForm.name || !this.spaceForm.name.trim()) {
      this.spaceFormError = 'Name is required';
      return;
    }
    const nameRegex = /^[a-zA-Z\s]+$/;
    if (this.spaceForm.name.trim().length < 5 || !nameRegex.test(this.spaceForm.name.trim())) {
      this.spaceFormError = 'Name must be at least 5 characters and contain only alphabets';
      return;
    }
    // Validate price: above 0 only
    if (this.spaceForm.pricePerHour === null || this.spaceForm.pricePerHour === undefined || this.spaceForm.pricePerHour === '') {
      this.spaceFormError = 'Price per hour is required';
      return;
    }
    if (this.spaceForm.pricePerHour <= 0) {
      this.spaceFormError = 'Price per hour must be greater than 0';
      return;
    }
    // Validate location: mandatory
    if (!this.spaceForm.location || !this.spaceForm.location.trim()) {
      this.spaceFormError = 'Location is required';
      return;
    }
    this.spaceFormLoading = true; this.spaceFormError = '';
    this.ownerSvc.addSpace(this.spaceForm).subscribe({
      next: () => { this.spaceFormLoading = false; this.showAddSpace = false; this.loadOwnerSpaces(); this.spaceForm = { name:'', type:'PUBLIC', pricePerHour:null, location:'', city:'', latitude:null, longitude:null, cctv:false, evCharging:false, guarded:false, coveredFence:false, notes:'' }; },
      error: err => { this.spaceFormLoading = false; this.spaceFormError = err?.error || 'Failed to add space'; }
    });
  }

  getOwnerReceiptInvoice(b: any) {
    this.receiptInvoice = null;
    this.showReceipt = b;
    this.showOwnerReceipt = true;
    this.bookingSvc.getInvoice([b.bookingId]).subscribe({ next: inv => this.receiptInvoice = inv, error: () => {} });
  }

  stars5 = [1,2,3,4,5];

  loadUnreadContactCount() {
    this.dashSvc.getUnreadContactCount().subscribe({
      next: n => this.unreadContactCount = n,
      error: () => {}
    });
  }

  openOwnerContacts() {
    this.showOwnerContacts = true;
    this.contactsLoading = true;
    this.dashSvc.getOwnerContacts().subscribe({
      next: r => { this.ownerContacts = r; this.contactsLoading = false; this.loadUnreadContactCount(); },
      error: () => this.contactsLoading = false
    });
  }

  markContactRead(req: any) {
    if (req.isRead) return;
    this.dashSvc.markContactRead(req.requestId).subscribe({
      next: () => { req.isRead = true; this.loadUnreadContactCount(); },
      error: () => {}
    });
  }

  loadOwnerApprovals() {
    this.ownerSvc.getApprovalRequests().subscribe({
      next: a => { this.ownerApprovals = a; this.approvalsOpen = true; },
      error: () => {}
    });
  }

  // Generate slots for public space
  openGenerateSlots(space: any) {
    this.showGenerateSlots = space;
    this.genLevels = null;
    this.genSlotsPerLevel = null;
    this.genError = '';
    this.genSuccess = '';
  }

  get genLevelsError(): string {
    if (this.genLevels === null) return '';
    if (this.genLevels < 1) return 'At least 1 level';
    if (this.genLevels > 10) return 'Maximum 10 levels';
    return '';
  }

  get genSlotsError(): string {
    if (this.genSlotsPerLevel === null) return '';
    if (this.genSlotsPerLevel < 1) return 'At least 1 slot';
    if (this.genSlotsPerLevel > 100) return 'Maximum 100 slots';
    return '';
  }

  submitGenerateSlots() {
    this.genError = '';
    if (!this.genLevels || !this.genSlotsPerLevel || this.genLevelsError || this.genSlotsError) {
      this.genError = 'Please fix the errors above'; return;
    }
    this.genLoading = true;
    this.ownerSvc.generateSlots(this.showGenerateSlots.spaceId, this.genLevels, this.genSlotsPerLevel).subscribe({
      next: () => {
        this.genLoading = false;
        this.genSuccess = `Generated ${this.genLevels! * this.genSlotsPerLevel!} slots across ${this.genLevels} levels.`;
      },
      error: err => { this.genLoading = false; this.genError = err?.error || 'Failed to generate slots'; }
    });
  }
}
