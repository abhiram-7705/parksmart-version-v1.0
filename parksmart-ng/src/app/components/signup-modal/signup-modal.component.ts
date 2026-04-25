import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ModalService } from '../../services/modal.service';

@Component({
  selector: 'app-signup-modal',
  templateUrl: './signup-modal.component.html',
  styleUrls: ['./signup-modal.component.css']
})
export class SignupModalComponent {
  userName = ''; email = ''; phoneNumber = ''; password = ''; confirmPassword = '';
  showPw = false; showCpw = false; loading = false; error = ''; success = '';
  touched: Record<string,boolean> = {};

  constructor(private auth: AuthService, private modal: ModalService) {}

  touch(f: string) { this.touched[f] = true; }

  getError(field: string): string {
    if (!this.touched[field]) return '';
    switch(field) {
      case 'userName': return !this.userName ? 'Username is required' : this.userName.length < 3 ? 'Min 3 characters' : '';
      case 'email': return !this.email ? 'Email is required' : !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email) ? 'Invalid email' : '';
      case 'phoneNumber': return !this.phoneNumber ? 'Phone is required' : !/^[1-9][0-9]{9}$/.test(this.phoneNumber) ? '10-digit number' : '';
      case 'password': return !this.password ? 'Password is required' : this.password.length < 8 ? 'Min 8 characters' : '';
      case 'confirmPassword': return !this.confirmPassword ? 'Required' : this.confirmPassword !== this.password ? 'Passwords do not match' : '';
    }
    return '';
  }

  get formValid(): boolean {
    return ['userName','email','phoneNumber','password','confirmPassword'].every(f => !this.getError(f) && (this as any)[f]);
  }

  close() { this.modal.close(); }
  goLogin() { this.modal.openLogin(); }
  onOverlay(e: Event) { if ((e.target as HTMLElement).classList.contains('modal-backdrop')) this.close(); }

  touchAll() { ['userName','email','phoneNumber','password','confirmPassword'].forEach(f => this.touched[f] = true); }

  submit() {
    this.touchAll();
    if (!this.formValid) return;
    this.loading = true; this.error = ''; this.success = '';
    this.auth.signup({ userName: this.userName, email: this.email, phoneNumber: this.phoneNumber, password: this.password, confirmPassword: this.confirmPassword }).subscribe({
      next: () => {
        this.loading = false;
        this.success = 'Account created! Redirecting to login…';
        setTimeout(() => this.modal.openLogin(), 1500);
      },
      error: err => { this.loading = false; this.error = err?.error || 'Registration failed. Please try again.'; }
    });
  }
}
