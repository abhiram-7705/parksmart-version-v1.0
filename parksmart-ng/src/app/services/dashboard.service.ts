import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BookingCard {
  bookingId: number; spaceName: string; city: string; spaceType: string;
  slotNumber: string; arrival: string; leaving: string; totalAmount: number;
  pricePerHour: number; status: string; latitude: number; longitude: number;
  isCancelable: boolean; isExtendable: boolean; isContactAllowed: boolean;
  isRatingAllowed: boolean; isReceiptAvailable: boolean;
  spaceId?: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  constructor(private http: HttpClient) {}
  getSuggestions(query: string): Observable<string[]> {
    return this.http.post<string[]>('/api/user-dashboard/bookings/suggestions', { query }, { withCredentials: true });
  }
  searchBookings(query?: string, status?: string[], types?: string[], sortBy?: string): Observable<BookingCard[]> {
    return this.http.post<BookingCard[]>('/api/user-dashboard/bookings/search', { query, status, types, sortBy }, { withCredentials: true });
  }
  cancelBooking(bookingId: number): Observable<string> {
    return this.http.post('/api/user-dashboard/bookings/cancel', { bookingId }, { withCredentials: true, responseType: 'text' });
  }
  submitRating(bookingId: number, rating: number, comment?: string): Observable<string> {
    return this.http.post('/api/user-dashboard/bookings/rate', { bookingId, rating, comment }, { withCredentials: true, responseType: 'text' });
  }
  submitContact(data: any): Observable<string> {
    return this.http.post('/api/user-dashboard/contacts/submit', data, { withCredentials: true, responseType: 'text' });
  }
}
