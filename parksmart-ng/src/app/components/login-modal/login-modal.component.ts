import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { ModalService } from '../../services/modal.service';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.css']
})
export class LoginModalComponent {
  email = ''; password = ''; rememberMe = false;
  showPw = false; loading = false; error = '';
  showForgot = false; forgotEmail = ''; forgotLoading = false; forgotMsg = ''; forgotError = '';

  emailTouched = false; passwordTouched = false;

  constructor(private auth: AuthService, private userSvc: UserService, private modal: ModalService, private router: Router) {}

  get emailError(): string {
    if (!this.emailTouched) return '';
    if (!this.email) return 'Email is required';
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email)) return 'Enter a valid email';
    return '';
  }
  get passwordError(): string {
    if (!this.passwordTouched) return '';
    if (!this.password) return 'Password is required';
    if (this.password.length < 8) return 'Minimum 8 characters';
    return '';
  }
  get formValid(): boolean { return !this.emailError && !this.passwordError && !!this.email && !!this.password; }

  close() { this.modal.close(); }
  onOverlay(e: Event) { if ((e.target as HTMLElement).classList.contains('modal-backdrop')) this.close(); }
  goSignup() { this.modal.openSignup(); }

  submit() {
    this.emailTouched = true; this.passwordTouched = true;
    if (!this.formValid) return;
    this.loading = true; this.error = '';
    this.auth.login({ email: this.email, password: this.password, rememberMe: this.rememberMe }).subscribe({
      next: () => {
        this.loading = false; this.modal.close();
        // Redirect based on user role
        this.userSvc.getProfile().subscribe(profile => {
          const route = profile.role === 'ADMIN' ? '/admin' : '/search';
          this.router.navigate([route]);
        });
      },
      error: err => { this.loading = false; this.error = err?.error || 'Invalid email or password'; }
    });
  }

  openForgot() { this.showForgot = true; this.forgotEmail = this.email; this.forgotMsg = ''; this.forgotError = ''; }
  closeForgot() { this.showForgot = false; }

  submitForgot() {
    if (!this.forgotEmail) { this.forgotError = 'Enter your email'; return; }
    this.forgotLoading = true; this.forgotError = ''; this.forgotMsg = '';
    setTimeout(() => {
      this.forgotLoading = false;
      this.forgotMsg = 'Feature still under development. Please try again later.';
    }, 800);
  }
}
