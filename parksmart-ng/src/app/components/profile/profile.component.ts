import { Component, OnInit } from '@angular/core';
import { UserService, UserProfile, WalletTx } from '../../services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: UserProfile | null = null;
  transactions: WalletTx[] = [];
  loading = true;
  walletOpen = true;
  showAddBalance = false;

  constructor(private userSvc: UserService) {}

  ngOnInit() {
    this.userSvc.getProfile().subscribe({ next: p => { this.profile = p; this.loading = false; }, error: () => this.loading = false });
    this.userSvc.getWallet().subscribe({ next: t => this.transactions = t, error: () => {} });
  }

  formatDate(d: string): string {
    return new Date(d).toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
