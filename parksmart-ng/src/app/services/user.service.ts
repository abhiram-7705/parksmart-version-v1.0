import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserProfile { userId: number; userName: string; email: string; balance: number; phoneNumber: string; role: string; }
export interface WalletTx { transactionId: number; amount: number; transactionType: string; description: string; transactionTime: string; }

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly BASE = '/api/user';
  constructor(private http: HttpClient) {}
  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(this.BASE + '/profile', { withCredentials: true });
  }
  getWallet(): Observable<WalletTx[]> {
    return this.http.get<WalletTx[]>(this.BASE + '/wallet', { withCredentials: true });
  }
}
