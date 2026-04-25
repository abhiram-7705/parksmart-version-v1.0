import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { BookingService, BookingSummary } from '../../services/booking.service';
import { ParkingService } from '../../services/parking.service';
import { ParkingCard } from '../../services/search.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-reservation-summary',
  templateUrl: './reservation-summary.component.html',
  styleUrls: ['./reservation-summary.component.css']
})
export class ReservationSummaryComponent implements OnInit, OnDestroy {
  card: ParkingCard | null = null;
  slotIds: number[] = [];
  arriving = '';
  leaving = '';
  summary: BookingSummary | null = null;
  vehicleNumber = '';
  promoCode = '';
  promoApplied = false;
  promoLoading = false;
  loading = true;
  confirmLoading = false;
  error = '';
  promoError = '';
  vehicleTouched = false;
  showConfirm = false;
  showInsufficientBalance = false;
  showAddBalance = false;
  timerDisplay = '';
  private holdExpiry: Date | null = null;
  private timerInterval: any;

  constructor(
    private bookingSvc: BookingService,
    private parkingSvc: ParkingService,
    private userSvc: UserService,
    private router: Router
  ) {}

  ngOnInit() {
    const state = history.state;
    this.card = state?.card;
    this.slotIds = state?.slotIds || [];
    this.arriving = state?.arriving || '';
    this.leaving = state?.leaving || '';
    if (state?.holdExpiry) {
      this.holdExpiry = new Date(state.holdExpiry);
      this.startTimer();
    }
    this.initiate();
  }

  ngOnDestroy() { clearInterval(this.timerInterval); }

  initiate() {
    if (!this.card || !this.slotIds.length) return;
    this.loading = true;
    // Use first slot hold — summary endpoint needs holdId from previous hold call
    // We'll re-hold here to get holdId
    this.parkingSvc.holdSlots(this.card.spaceId, this.slotIds, this.arriving, this.leaving).subscribe({
      next: holdRes => {
        this.holdExpiry = new Date(holdRes.expiresAt);
        this.startTimer();
        this.bookingSvc.initiate(holdRes.holdId, this.card!.spaceId, this.arriving, this.leaving).subscribe({
          next: s => { this.summary = s; this.summary.holdId = holdRes.holdId; this.loading = false; },
          error: err => { this.error = err?.error || 'Failed to load booking summary'; this.loading = false; }
        });
      },
      error: err => { this.error = err?.error || 'Could not hold slots'; this.loading = false; }
    });
  }

  startTimer() {
    clearInterval(this.timerInterval);
    this.timerInterval = setInterval(() => {
      if (!this.holdExpiry) return;
      const diff = this.holdExpiry.getTime() - Date.now();
      if (diff <= 0) { clearInterval(this.timerInterval); this.timerDisplay = '0:00'; return; }
      const m = Math.floor(diff / 60000);
      const s = Math.floor((diff % 60000) / 1000);
      this.timerDisplay = `${m}:${String(s).padStart(2,'0')}`;
    }, 1000);
  }

  applyPromo() {
    if (!this.promoCode || !this.summary) return;
    this.promoLoading = true; this.promoError = '';
    this.bookingSvc.applyPromo(this.summary.holdId, this.promoCode, this.card!.spaceId, this.arriving, this.leaving).subscribe({
      next: s => { this.summary = s; this.promoApplied = true; this.promoLoading = false; },
      error: err => { this.promoError = err?.error || 'Invalid promo code'; this.promoLoading = false; }
    });
  }

  extendHold() {
    if (!this.summary) return;
    this.bookingSvc.extendHold(this.summary.holdId).subscribe({
      next: newExpiry => {
        this.holdExpiry = new Date(newExpiry);
        this.startTimer();
      }
    });
  }

  get vehicleError(): string {
    if (!this.vehicleTouched) return '';
    if (!this.vehicleNumber) return 'Vehicle number is required';
    if (!/^[A-Z]{2}[0-9]{1,2}[A-Z]{1,3}[0-9]{4}$/.test(this.vehicleNumber.toUpperCase())) return 'Invalid format (e.g. TN09AB1234)';
    return '';
  }

  openConfirm() {
    this.vehicleTouched = true;
    if (this.vehicleError) return;
    if (!this.summary) return;
    if (!this.summary.sufficientBalance) { this.showInsufficientBalance = true; return; }
    this.showConfirm = true;
  }

  confirm() {
    if (!this.summary) return;
    this.showConfirm = false; this.confirmLoading = true;
    this.bookingSvc.confirm(this.summary.holdId, this.card!.spaceId, this.arriving, this.leaving,
      this.vehicleNumber.toUpperCase(), this.promoApplied ? this.promoCode : undefined).subscribe({
      next: res => {
        this.confirmLoading = false;
        this.router.navigate(['/booking/confirm'], { state: { bookingIds: res.bookingIds, card: this.card } });
      },
      error: err => { this.confirmLoading = false; this.error = err?.error || 'Booking failed'; }
    });
  }

  releaseAndGoBack() {
    if (this.summary?.holdId) {
      this.parkingSvc.releaseSlots(this.summary.holdId).subscribe(() => this.router.navigate(['/search']));
    } else {
      this.router.navigate(['/search']);
    }
  }

  formatDt(dt: string): string {
    if (!dt) return '';
    return new Date(dt).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' });
  }
}
