import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private userSvc: UserService, private router: Router) {}
  canActivate(): Observable<boolean> {
    return this.userSvc.getProfile().pipe(
      map(p => {
        if (p.role === 'ADMIN') return true;
        this.router.navigate(['/search']);
        return false;
      }),
      catchError(() => { this.router.navigate(['/search']); return of(false); })
    );
  }
}
