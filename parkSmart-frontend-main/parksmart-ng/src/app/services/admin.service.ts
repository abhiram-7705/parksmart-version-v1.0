import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdminDashboard { pendingWalletRequests: number; pendingSpaceApprovals: number; activePromoCodes: number; }
export interface WalletReq { requestId: number; userName: string; email: string; amount: number; status: string; requestedAt: string; resolvedAt: string; }
export interface PromoDTO { promoId: number; code: string; discountPercentage: number; isActive: boolean; expiryDate: string; }
export interface SpaceApproval { approvalId: number; spaceId: number; ownerName: string; ownerEmail: string; status: string; rejectionReason: string; submittedAt: string; resolvedAt: string; name: string; type: string; price: number; location: string; city: string; latitude: number; longitude: number; cctv: boolean; ev: boolean; guarded: boolean; covered: boolean; notes: string; }

@Injectable({ providedIn: 'root' })
export class AdminService {
  private B = '/api/admin';
  constructor(private http: HttpClient) {}

  getDashboard(): Observable<AdminDashboard> { return this.http.post<AdminDashboard>(this.B + '/dashboard', {}, { withCredentials: true }); }

  getWalletRequests(): Observable<WalletReq[]> { return this.http.post<WalletReq[]>(this.B + '/wallet/requests', {}, { withCredentials: true }); }
  approveWallet(id: number): Observable<string> { return this.http.post(this.B + '/wallet/approve', { id }, { withCredentials: true, responseType: 'text' }); }
  rejectWallet(id: number): Observable<string> { return this.http.post(this.B + '/wallet/reject', { id }, { withCredentials: true, responseType: 'text' }); }

  getPromos(): Observable<PromoDTO[]> { return this.http.post<PromoDTO[]>(this.B + '/promos', {}, { withCredentials: true }); }
  createPromo(data: any): Observable<PromoDTO> { return this.http.post<PromoDTO>(this.B + '/promos/create', data, { withCredentials: true }); }
  togglePromo(id: number): Observable<PromoDTO> { return this.http.post<PromoDTO>(this.B + '/promos/toggle', { id }, { withCredentials: true }); }
  deletePromo(id: number): Observable<string> { return this.http.post(this.B + '/promos/delete', { id }, { withCredentials: true, responseType: 'text' }); }

  getSpaceApprovals(): Observable<SpaceApproval[]> { return this.http.post<SpaceApproval[]>(this.B + '/spaces/approvals', {}, { withCredentials: true }); }
  approveSpace(id: number): Observable<string> { return this.http.post(this.B + '/spaces/approve', { id }, { withCredentials: true, responseType: 'text' }); }
  rejectSpace(id: number, reason: string): Observable<string> { return this.http.post(this.B + '/spaces/reject', { id, reason }, { withCredentials: true, responseType: 'text' }); }
}
