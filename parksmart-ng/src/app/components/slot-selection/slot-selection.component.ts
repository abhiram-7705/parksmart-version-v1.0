import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ParkingService, SlotInfo } from '../../services/parking.service';
import { ParkingCard } from '../../services/search.service';

@Component({
  selector: 'app-slot-selection',
  templateUrl: './slot-selection.component.html',
  styleUrls: ['./slot-selection.component.css']
})
export class SlotSelectionComponent implements OnInit, OnDestroy {
  card: ParkingCard | null = null;
  spaceId = 0;
  arriving = '';
  leaving = '';
  slots: SlotInfo[] = [];
  selectedSlotIds: number[] = [];
  loading = false;
  error = '';
  durationMinutes = 0;
  billedHours = 0;
  totalPrice = 0;
  holdExpiry: Date | null = null;
  timerDisplay = '';
  private timerInterval: any;
  showExpiredDialog = false;
  expiredCountdown = 10;
  private expiredInterval: any;

  constructor(private route: ActivatedRoute, public router: Router, private parkingSvc: ParkingService) {}

  ngOnInit() {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as any || history.state;
    if (state?.card) {
      this.card = state.card;
      this.spaceId = state.card.spaceId;
    } else {
      this.spaceId = Number(this.route.snapshot.paramMap.get('spaceId'));
    }
    if (state?.arriving) this.arriving = state.arriving;
    else { const now = new Date(); this.arriving = this.toLocal(new Date(now.getTime() + 15*60000)); }
    if (state?.leaving) this.leaving = state.leaving;
    else { const a = new Date(this.arriving); this.leaving = this.toLocal(new Date(a.getTime() + 15*60000)); }
    this.ensureMinArrival();
    this.fetchSlots();
  }

  ngOnDestroy() { clearInterval(this.timerInterval); clearInterval(this.expiredInterval); }

  toLocal(d: Date): string {
    const p = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${p(d.getMonth()+1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
  }

  ensureMinArrival() {
    const min = new Date(Date.now() + 15*60000);
    const arr = new Date(this.arriving);
    if (arr < min) this.arriving = this.toLocal(min);
  }

  onTimeChange() {
    this.ensureMinArrival();
    const arr = new Date(this.arriving);
    const lv = new Date(this.leaving);
    if (lv <= arr) this.leaving = this.toLocal(new Date(arr.getTime() + 15*60000));
    this.selectedSlotIds = [];
    this.holdExpiry = null;
    clearInterval(this.timerInterval);
    this.timerDisplay = '';
    this.fetchSlots();
  }

  fetchSlots() {
    if (!this.spaceId || !this.arriving || !this.leaving) return;
    this.loading = true;
    this.parkingSvc.getAvailability(this.spaceId, this.arriving, this.leaving).subscribe({
      next: res => {
        this.slots = res.slots;
        this.durationMinutes = res.durationMinutes;
        this.billedHours = res.billedHours;
        this.totalPrice = res.totalPrice;
        this.loading = false;
      },
      error: err => { this.error = err?.error || 'Failed to load slots'; this.loading = false; }
    });
  }

  toggleSlot(slot: SlotInfo) {
    if (!slot.isAvailable) return;
    const idx = this.selectedSlotIds.indexOf(slot.slotId);
    if (idx >= 0) {
      this.selectedSlotIds.splice(idx, 1);
      if (this.selectedSlotIds.length === 0) {
        this.holdExpiry = null;
        clearInterval(this.timerInterval);
        this.timerDisplay = '';
      }
    } else {
      if (this.selectedSlotIds.length >= 3) return;
      this.selectedSlotIds.push(slot.slotId);
      this.placeHold();
    }
  }

  placeHold() {
    this.parkingSvc.holdSlots(this.spaceId, this.selectedSlotIds, this.arriving, this.leaving).subscribe({
      next: res => {
        this.holdExpiry = new Date(res.expiresAt);
        clearInterval(this.timerInterval);
        this.startTimer();
      },
      error: () => {}
    });
  }

  startTimer() {
    this.timerInterval = setInterval(() => {
      if (!this.holdExpiry) return;
      const diff = this.holdExpiry.getTime() - Date.now();
      if (diff <= 0) {
        clearInterval(this.timerInterval);
        this.timerDisplay = '0:00';
        this.showExpiredModal();
        return;
      }
      const m = Math.floor(diff / 60000);
      const s = Math.floor((diff % 60000) / 1000);
      this.timerDisplay = `${m}:${String(s).padStart(2,'0')}`;
    }, 1000);
  }

  showExpiredModal() {
    this.showExpiredDialog = true;
    this.expiredCountdown = 10;
    this.expiredInterval = setInterval(() => {
      this.expiredCountdown--;
      if (this.expiredCountdown <= 0) {
        clearInterval(this.expiredInterval);
        this.router.navigate(['/search']);
      }
    }, 1000);
  }

  isSelected(slotId: number): boolean { return this.selectedSlotIds.includes(slotId); }
  getSlotClass(slot: SlotInfo): string {
    if (this.isSelected(slot.slotId)) return 'selected';
    if (!slot.isAvailable) return 'unavailable';
    return 'available';
  }

  proceed() {
    if (this.selectedSlotIds.length === 0) return;
    const holdId = ''; // will be obtained from last hold response
    this.router.navigate(['/booking'], {
      state: { card: this.card, slotIds: this.selectedSlotIds, arriving: this.arriving, leaving: this.leaving, holdExpiry: this.holdExpiry?.toISOString() }
    });
  }

  goBack() { this.router.navigate(['/search']); }

  availableCount(): number { return this.slots.filter(s => s.isAvailable).length; }
}
