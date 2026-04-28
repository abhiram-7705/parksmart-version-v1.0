import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface LoginRequest { email: string; password: string; rememberMe: boolean; }
export interface SignUpRequest { userName: string; email: string; password: string; confirmPassword: string; phoneNumber: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly BASE = '/api/auth';
  private _loggedIn = new BehaviorSubject<boolean>(false);
  loggedIn$ = this._loggedIn.asObservable();

  constructor(private http: HttpClient) {}

  login(r: LoginRequest): Observable<string> {
    return this.http.post(this.BASE + '/login', r, { withCredentials: true, responseType: 'text' })
      .pipe(tap(() => this._loggedIn.next(true)));
  }
  signup(r: SignUpRequest): Observable<string> {
    return this.http.post(this.BASE + '/signup', r, { withCredentials: true, responseType: 'text' });
  }
  logout(): Observable<void> {
    return this.http.post<void>(this.BASE + '/logout', {}, { withCredentials: true })
      .pipe(tap(() => this._loggedIn.next(false)));
  }
  forgotPassword(email: string): Observable<string> {
    return this.http.post(this.BASE + '/forgot-password', { email }, { withCredentials: true, responseType: 'text' });
  }
  setLoggedIn(v: boolean) { this._loggedIn.next(v); }
  isLoggedIn(): boolean { return this._loggedIn.getValue(); }
}
