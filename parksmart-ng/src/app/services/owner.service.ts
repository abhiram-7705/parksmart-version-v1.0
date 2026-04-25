import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OwnerSlot { slotId: number; slotNumber: string; status: string; vehicleNumber?: string; hasActiveBookings: boolean; }
export interface OwnerBooking { bookingId: number; slotNumber: string; vehicleNumber: string; arrival: string; leaving: string; bookedAt: string; amount: number; refundable: boolean; status: string; }
export interface TimelineSlot { slotNumber: string; bookings: { vehicleNumber: string; arrival: string; leaving: string; status: string; }[]; }
export interface SpaceDetail { spaceId: number; name: string; type: string; location: string; city: string; pricePerHour: number; latitude: number; longitude: number; facilities: string[]; notes: string; active: boolean; rating: number; }

@Injectable({ providedIn: 'root' })
export class OwnerService {
  private readonly BASE = '/api/owner-dashboard';
  constructor(private http: HttpClient) {}
  getSpaces(): Observable<any[]> {
    return this.http.post<any[]>(this.BASE + '/dashboard/cards', {}, { withCredentials: true });
  }
  addSpace(data: any): Observable<any> {
    return this.http.post<any>(this.BASE + '/spaces/add', data, { withCredentials: true });
  }
  editSpace(data: any): Observable<any> {
    return this.http.post<any>(this.BASE + '/spaces/edit', data, { withCredentials: true });
  }
  deleteSpace(spaceId: number): Observable<string> {
    return this.http.post(this.BASE + '/spaces/delete', { spaceId }, { withCredentials: true, responseType: 'text' });
  }
  getSpaceDetail(spaceId: number): Observable<SpaceDetail> {
    return this.http.post<SpaceDetail>(this.BASE + '/spaces/detail', { spaceId }, { withCredentials: true });
  }
  getSlots(spaceId: number): Observable<OwnerSlot[]> {
    return this.http.post<OwnerSlot[]>(this.BASE + '/slots', { spaceId }, { withCredentials: true });
  }
  addSlot(spaceId: number, slotNumber: string): Observable<OwnerSlot> {
    return this.http.post<OwnerSlot>(this.BASE + '/slots/add', { spaceId, slotNumber }, { withCredentials: true });
  }
  toggleSlot(slotId: number): Observable<void> {
    return this.http.post<void>(this.BASE + '/slots/toggle-status', { slotId }, { withCredentials: true });
  }
  deleteSlot(slotId: number): Observable<string> {
    return this.http.post(this.BASE + '/slots/delete', { slotId }, { withCredentials: true, responseType: 'text' });
  }
  getBookings(spaceId: number, status?: string): Observable<OwnerBooking[]> {
    return this.http.post<OwnerBooking[]>(this.BASE + '/bookings', { spaceId, status }, { withCredentials: true });
  }
  getTimeline(spaceId: number, date: string): Observable<TimelineSlot[]> {
    return this.http.post<TimelineSlot[]>(this.BASE + '/timeline', { spaceId, date }, { withCredentials: true });
  }
}
