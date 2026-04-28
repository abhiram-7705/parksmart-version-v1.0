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

  // Public parking
  get isPublicSpace(): boolean { return this.card?.type === 'PUBLIC'; }
  get groupedSlots(): { level: string; slots: any[] }[] {
    if (!this.isPublicSpace) return [];
    const map: { [key: string]: any[] } = {};
    for (const slot of this.slots) {
      const match = slot.slotNumber.match(/^(L\d+)-/);
      const level = match ? match[1] : 'Other';
      if (!map[level]) map[level] = [];
      map[level].push(slot);
    }
    return Object.keys(map).sort().map(k => ({ level: k, slots: map[k] }));
  }
  activeLevel = '';

  selectLevel(level: string) { this.activeLevel = level; }

  // Date/Time selector state
  selectedDate = '';
  selectedHour = 0;
  selectedQuarter = 0; // 0,1,2,3 → :00, :15, :30, :45
  leavingHour = 0;
  leavingQuarter = 0;
  dateError = '';
  today = '';
  hours: number[] = Array.from({ length: 24 }, (_, i) => i);
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

    // Default: today's date, next 15-min quadrant + 15 min buffer, leaving +1hr
    const now = new Date();
    this.today = this.formatDateOnly(now);
    this.selectedDate = this.today;

    // Compute default arrival: next quadrant from now+15min
    const arrivalDefault = new Date(now.getTime() + 16 * 60000);
    const defaultArrival = this.snapToNextQuadrant(arrivalDefault);
    this.selectedHour = defaultArrival.hour;
    this.selectedQuarter = defaultArrival.quarter;

    // Leaving = arrival + 1 hour
    const leavingDefault = this.addMinutes(defaultArrival, 60);
    this.leavingHour = leavingDefault.hour;
    this.leavingQuarter = leavingDefault.quarter;

    // If state passed arriving/leaving (from extend flow), parse and apply
    if (state?.arriving) {
      const d = new Date(state.arriving);
      this.selectedDate = this.formatDateOnly(d);
      this.selectedHour = d.getHours();
      this.selectedQuarter = Math.floor(d.getMinutes() / 15);
      const lv = state.leaving ? new Date(state.leaving) : new Date(d.getTime() + 3600000);
      this.leavingHour = lv.getHours();
      this.leavingQuarter = Math.floor(lv.getMinutes() / 15);
    }

    this.syncTimeStrings();
    this.fetchSlots();
  }

  ngOnDestroy() { clearInterval(this.timerInterval); clearInterval(this.expiredInterval); }

  formatDateOnly(d: Date): string {
    const p = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`;
  }

  toLocal(d: Date): string {
    const p = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${p(d.getMonth()+1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
  }

  snapToNextQuadrant(d: Date): { hour: number; quarter: number } {
    const mins = d.getMinutes();
    const nextQ = Math.ceil(mins / 15);
    if (nextQ >= 4) {
      return { hour: (d.getHours() + 1) % 24, quarter: 0 };
    }
    return { hour: d.getHours(), quarter: nextQ };
  }

  addMinutes(t: { hour: number; quarter: number }, mins: number): { hour: number; quarter: number } {
    const totalMins = t.hour * 60 + t.quarter * 15 + mins;
    return { hour: Math.floor(totalMins / 60) % 24, quarter: Math.floor((totalMins % 60) / 15) };
  }

  quarterToMins(q: number): number { return q * 15; }

  syncTimeStrings() {
    const p = (n: number) => String(n).padStart(2, '0');
    this.arriving = `${this.selectedDate}T${p(this.selectedHour)}:${p(this.quarterToMins(this.selectedQuarter))}`;
    this.leaving = `${this.selectedDate}T${p(this.leavingHour)}:${p(this.quarterToMins(this.leavingQuarter))}`;
    // Handle leaving going past midnight
    if (this.leavingHour < this.selectedHour ||
        (this.leavingHour === this.selectedHour && this.leavingQuarter <= this.selectedQuarter)) {
      // leaving is next day
      const base = new Date(this.selectedDate);
      base.setDate(base.getDate() + 1);
      this.leaving = `${this.formatDateOnly(base)}T${p(this.leavingHour)}:${p(this.quarterToMins(this.leavingQuarter))}`;
    }
  }

  validateAndFetch() {
    this.dateError = '';
    const now = new Date();
    const arrivalDt = new Date(this.arriving);
    const leavingDt = new Date(this.leaving);
    const minArrival = new Date(now.getTime() + 14 * 60000); // 14 min buffer

    if (this.selectedDate < this.today) {
      this.dateError = 'Cannot select a past date.';
      return;
    }
    if (arrivalDt < minArrival) {
      this.dateError = 'Arrival must be at least 15 minutes from now.';
      return;
    }
    if (leavingDt <= arrivalDt) {
      this.dateError = 'Leaving time must be after arrival time.';
      return;
    }
    this.selectedSlotIds = [];
    this.holdExpiry = null;
    clearInterval(this.timerInterval);
    this.timerDisplay = '';
    this.fetchSlots();
  }

  onDateChange() {
    // If selected date is today, ensure arrival is still valid
    if (this.selectedDate === this.today) {
      const now = new Date();
      const snap = this.snapToNextQuadrant(new Date(now.getTime() + 15 * 60000));
      const currentArrivalMins = this.selectedHour * 60 + this.selectedQuarter * 15;
      const minMins = snap.hour * 60 + snap.quarter * 15;
      if (currentArrivalMins < minMins) {
        this.selectedHour = snap.hour;
        this.selectedQuarter = snap.quarter;
        const lv = this.addMinutes({ hour: snap.hour, quarter: snap.quarter }, 60);
        this.leavingHour = lv.hour;
        this.leavingQuarter = lv.quarter;
      }
    }
    this.syncTimeStrings();
    this.validateAndFetch();
  }

onArrivalChange() {
  this.dateError = '';
  const lv = this.addMinutes({ hour: this.selectedHour, quarter: this.selectedQuarter }, 60);
  this.leavingHour = lv.hour;
  this.leavingQuarter = lv.quarter;
  this.syncTimeStrings();
  this.validateAndFetch();
}

onLeavingChange() {
  this.dateError = '';
  this.syncTimeStrings();
  this.validateAndFetch();
}

  isQuarterPast(h: number, q: number): boolean {
    if (this.selectedDate > this.today) return false;
    const now = new Date();
    const minArrival = new Date(now.getTime() + 14 * 60000);
    const slotMins = h * 60 + q * 15;
    const minMins = minArrival.getHours() * 60 + minArrival.getMinutes();
    return slotMins < minMins;
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
        // Set default level for public spaces
        if (this.isPublicSpace && this.groupedSlots.length > 0 && !this.activeLevel) {
          this.activeLevel = this.groupedSlots[0].level;
        }
      },
      error: err => { this.error = err?.error || 'Failed to load slots'; this.loading = false; }
    });
  }

  toggleSlot(slot: SlotInfo) {
    if (!slot.available) return;
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

  getDateChip(offset: number): { value: string; dayName: string; dateNum: string; monthName: string } {
    const d = new Date();
    d.setDate(d.getDate() + offset);
    const days = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    return {
      value: this.formatDateOnly(d),
      dayName: days[d.getDay()],
      dateNum: String(d.getDate()),
      monthName: months[d.getMonth()]
    };
  }

  formatTimeDisplay(h: number, q: number): string {
    const ampm = h >= 12 ? 'PM' : 'AM';
    const disp = h % 12 || 12;
    return `${disp}:${String(q * 15).padStart(2,'0')} ${ampm}`;
  }

  getSlotClass(slot: SlotInfo): string {
    if (this.isSelected(slot.slotId)) return 'selected';
    if (!slot.available) {
      if (slot.status === 'BLOCKED') return 'blocked';
      return 'unavailable';
    }
    return 'available';
  }

  proceed() {
    if (this.selectedSlotIds.length === 0) return;
    this.router.navigate(['/booking'], {
      state: { card: this.card, slotIds: this.selectedSlotIds, arriving: this.arriving, leaving: this.leaving, holdExpiry: this.holdExpiry?.toISOString() }
    });
  }

  goBack() { this.router.navigate(['/search']); }
  availableCount(): number { return this.slots.filter(s => s.available).length; }

  formatHour(h: number): string {
    const ampm = h >= 12 ? 'PM' : 'AM';
    const disp = h % 12 || 12;
    return `${disp} ${ampm}`;
  }

  quarterLabel(q: number): string {
    return `:${String(q * 15).padStart(2, '0')}`;
  }

  isArrival(h: number, q: number): boolean {
    return this.selectedHour === h && this.selectedQuarter === q;
  }

  isLeaving(h: number, q: number): boolean {
    return this.leavingHour === h && this.leavingQuarter === q;
  }

  isInRange(h: number, q: number): boolean {
    const slot = h * 60 + q * 15;
    const arr = this.selectedHour * 60 + this.selectedQuarter * 15;
    const lv = this.leavingHour * 60 + this.leavingQuarter * 15;
    // Handle overnight
    if (lv <= arr) return slot >= arr || slot < lv;
    return slot > arr && slot < lv;
  }

}
