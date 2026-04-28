import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SlotInfo { slotId: number; slotNumber: string; available: boolean; status: string; }
export interface SlotAvailResponse { slots: SlotInfo[]; durationMinutes: number; billedHours: number; totalPrice: number; }
export interface HoldResponse { holdId: string; expiresAt: string; }

@Injectable({ providedIn: 'root' })
export class ParkingService {
  constructor(private http: HttpClient) {}
  getAvailability(spaceId: number, arrival: string, leaving: string): Observable<SlotAvailResponse> {
    return this.http.post<SlotAvailResponse>('/api/parking/slots/availability', { spaceId, arrival, leaving }, { withCredentials: true });
  }
  holdSlots(spaceId: number, slotIds: number[], arrival: string, leaving: string): Observable<HoldResponse> {
    return this.http.post<HoldResponse>('/api/parking/slots/hold', { spaceId, slotIds, arrival, leaving }, { withCredentials: true });
  }
  releaseSlots(holdId: string): Observable<string> {
    return this.http.post('/api/parking/slots/release', { holdId }, { withCredentials: true, responseType: 'text' });
  }
}
