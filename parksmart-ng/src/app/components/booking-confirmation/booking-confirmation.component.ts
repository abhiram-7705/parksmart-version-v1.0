import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { BookingService, InvoiceResponse } from '../../services/booking.service';
import { ParkingCard } from '../../services/search.service';

@Component({
  selector: 'app-booking-confirmation',
  templateUrl: './booking-confirmation.component.html',
  styleUrls: ['./booking-confirmation.component.css']
})
export class BookingConfirmationComponent implements OnInit, OnDestroy {
  processing = true;
  invoice: InvoiceResponse | null = null;
  card: ParkingCard | null = null;
  countdown = 30;
  private countInterval: any;

  constructor(private bookingSvc: BookingService, private router: Router) {}

  ngOnInit() {
    const state = history.state;
    this.card = state?.card;
    const bookingIds: number[] = state?.bookingIds || [];

    // Show processing for 3s
    setTimeout(() => {
      this.processing = false;
      if (bookingIds.length > 0) {
        this.bookingSvc.getInvoice(bookingIds).subscribe({
          next: inv => { this.invoice = inv; this.startCountdown(); },
          error: () => { this.startCountdown(); }
        });
      } else {
        this.startCountdown();
      }
    }, 3000);
  }

  ngOnDestroy() { clearInterval(this.countInterval); }

  startCountdown() {
    this.countInterval = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) { clearInterval(this.countInterval); this.router.navigate(['/dashboard']); }
    }, 1000);
  }

  goDashboard() { clearInterval(this.countInterval); this.router.navigate(['/dashboard']); }

  formatDt(dt: string): string {
    if (!dt) return '';
    return new Date(dt).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' });
  }
}
