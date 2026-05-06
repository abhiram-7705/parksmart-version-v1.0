import { Component, OnInit } from '@angular/core';
import { UserService, UserProfile, WalletTx, WalletRequest } from '../../services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: UserProfile | null = null;
  transactions: WalletTx[] = [];
  walletRequests: WalletRequest[] = [];
  loading = true;
  walletOpen = true;
  requestsOpen = false;
  showAddBalance = false;

  requestAmount: number | null = null;
  requestError = '';
  requestLoading = false;
  requestSuccess = '';
  hasPending = false;

  constructor(private userSvc: UserService) {}

  ngOnInit() {
    this.userSvc.getProfile().subscribe({ next: p => { this.profile = p; this.loading = false; }, error: () => this.loading = false });
    this.userSvc.getWallet().subscribe({ next: t => this.transactions = t, error: () => {} });
    this.loadWalletRequests();
  }

  loadWalletRequests() {
    this.userSvc.getWalletRequests().subscribe({
      next: r => {
        this.walletRequests = r;
        this.hasPending = r.some(x => x.status === 'PENDING');
      },
      error: () => {}
    });
  }

  get requestAmountError(): string {
    if (this.requestAmount === null) return '';
    if (this.requestAmount < 10) return 'Minimum amount is ₹10';
    if (this.requestAmount > 2000) return 'Maximum amount is ₹2000';
    return '';
  }

  submitRequest() {
    this.requestError = ''; this.requestSuccess = '';
    if (!this.requestAmount || this.requestAmountError) { this.requestError = this.requestAmountError || 'Enter a valid amount'; return; }
    this.requestLoading = true;
    this.userSvc.raiseWalletRequest(this.requestAmount).subscribe({
      next: () => {
        this.requestLoading = false;
        this.requestSuccess = 'Request submitted! Admin will review shortly.';
        this.requestAmount = null;
        this.showAddBalance = false;
        this.loadWalletRequests();
        this.requestsOpen = true;
      },
      error: err => { this.requestLoading = false; this.requestError = err?.error || 'Failed to submit request'; }
    });
  }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
