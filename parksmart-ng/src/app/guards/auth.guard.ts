import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private userService: UserService, private authService: AuthService, private router: Router) {}
  canActivate(): Observable<boolean> {
    return this.userService.getProfile().pipe(
      tap(() => this.authService.setLoggedIn(true)),
      map(() => true),
      catchError(() => { this.authService.setLoggedIn(false); this.router.navigate(['/']); return of(false); })
    );
  }
}
