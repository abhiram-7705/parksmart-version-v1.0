import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BookingSummary {
  holdId: string;
  spaceId: number;
  slotIds?: number[];
  arrival: string;
  leaving: string;
  baseRate: number;
  durationHours: number;
  subtotal: number;
  discount: number;
  tax: number;
  total: number;
  walletBalance: number;
  sufficientBalance: boolean;
  holdExpiry: string;
}
export interface BookingConfirmResponse { bookingIds: number[]; status: string; }
export interface InvoiceResponse {
  spaceName: string; location: string; slotNumbers: string[];
  arrival: string; leaving: string; durationHours: number;
  vehicleNumber: string; baseRate: number; subtotal: number;
  tax: number; discount: number; totalAmount: number; status: string;
}

@Injectable({ providedIn: 'root' })
export class BookingService {
  constructor(private http: HttpClient) {}
  initiate(holdId: string, spaceId: number, arrival: string, leaving: string): Observable<BookingSummary> {
    return this.http.post<BookingSummary>('/api/bookings/bookings/initiate', { holdId, spaceId, arrival, leaving }, { withCredentials: true });
  }
  applyPromo(holdId: string, promoCode: string, spaceId: number, arrival: string, leaving: string): Observable<BookingSummary> {
    return this.http.post<BookingSummary>('/api/bookings/bookings/apply-promo', { holdId, promoCode, spaceId, arrival, leaving }, { withCredentials: true });
  }
  extendHold(holdId: string): Observable<string> {
    return this.http.post('/api/bookings/bookings/extend-hold', { holdId }, { withCredentials: true, responseType: 'text' });
  }
  confirm(holdId: string, spaceId: number, arrival: string, leaving: string, vehicleNumber: string, promoCode?: string): Observable<BookingConfirmResponse> {
    return this.http.post<BookingConfirmResponse>('/api/bookings/booking/confirm', { holdId, spaceId, arrival, leaving, vehicleNumber, promoCode }, { withCredentials: true });
  }
  getInvoice(bookingIds: number[]): Observable<InvoiceResponse> {
    return this.http.post<InvoiceResponse>('/api/bookings/bookings/invoice', { bookingIds }, { withCredentials: true });
  }
}
