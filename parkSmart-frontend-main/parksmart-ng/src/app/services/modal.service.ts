import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
export type ModalType = 'login' | 'signup' | null;
@Injectable({ providedIn: 'root' })
export class ModalService {
  private _modal = new BehaviorSubject<ModalType>(null);
  modal$ = this._modal.asObservable();
  openLogin() { this._modal.next('login'); }
  openSignup() { this._modal.next('signup'); }
  close() { this._modal.next(null); }
}
