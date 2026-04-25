import { Component, OnInit } from '@angular/core';
import { UserService } from './services/user.service';
import { AuthService } from './services/auth.service';
import { ModalService, ModalType } from './services/modal.service';

@Component({
  selector: 'app-root',
  template: `
    <app-navbar></app-navbar>
    <router-outlet></router-outlet>
    <app-login-modal *ngIf="modal === 'login'"></app-login-modal>
    <app-signup-modal *ngIf="modal === 'signup'"></app-signup-modal>
  `,
  styles: []
})
export class AppComponent implements OnInit {
  modal: ModalType = null;

  constructor(
    private userSvc: UserService,
    private authSvc: AuthService,
    private modalSvc: ModalService
  ) {}

  ngOnInit() {
    this.modalSvc.modal$.subscribe(m => this.modal = m);
    this.userSvc.getProfile().subscribe({
      next: () => this.authSvc.setLoggedIn(true),
      error: () => this.authSvc.setLoggedIn(false)
    });
  }
}
