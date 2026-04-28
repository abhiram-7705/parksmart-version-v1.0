import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AdminService, AdminDashboard, WalletReq, PromoDTO, SpaceApproval } from '../../services/admin.service';
import { UserService, UserProfile } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  activeSection: 'wallets' | 'spaces' | 'promos' = 'wallets';
  profile: UserProfile | null = null;
  profileOpen = false;

  // Dashboard counts for nav badges
  dashData: AdminDashboard | null = null;

  // Wallet requests
  walletRequests: WalletReq[] = [];
  walletFilter = 'ALL';
  walletLoading = false;

  // Space approvals
  spaceApprovals: SpaceApproval[] = [];
  spaceFilter = 'ALL';
  spacesLoading = false;
  rejectSpaceId: number | null = null;
  rejectReason = '';
  showRejectModal = false;

  // Promos
  promos: PromoDTO[] = [];
  promosLoading = false;
  showPromoForm = false;
  promoForm: any = { code: '', discountPercentage: null, expiryDate: '' };
  promoFormError = '';
  promoFormLoading = false;
  deletePromoConfirm: PromoDTO | null = null;

  constructor(
    private adminSvc: AdminService,
    private userSvc: UserService,
    private authSvc: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.userSvc.getProfile().subscribe({ next: p => this.profile = p, error: () => {} });
    this.loadDashboardCounts();
    this.navigate('wallets');
  }

  @HostListener('document:click', ['$event']) onDocClick(e: Event) {
    if (!(e.target as HTMLElement).closest('.admin-profile-wrap')) this.profileOpen = false;
  }

  loadDashboardCounts() {
    this.adminSvc.getDashboard().subscribe({
      next: d => this.dashData = d,
      error: () => {}
    });
  }

  navigate(section: 'wallets' | 'spaces' | 'promos') {
    this.activeSection = section;
    if (section === 'wallets') this.loadWallets();
    if (section === 'spaces') this.loadSpaces();
    if (section === 'promos') this.loadPromos();
  }

  // ── Wallets ──────────────────────────────────────────────────────────────
  loadWallets() {
    this.walletLoading = true;
    this.adminSvc.getWalletRequests().subscribe({
      next: r => { this.walletRequests = r; this.walletLoading = false; },
      error: () => this.walletLoading = false
    });
  }

  get filteredWallets(): WalletReq[] {
    if (this.walletFilter === 'ALL') return this.walletRequests;
    return this.walletRequests.filter(r => r.status === this.walletFilter);
  }

  approveWallet(req: WalletReq) {
    this.adminSvc.approveWallet(req.requestId).subscribe({
      next: () => { this.loadWallets(); this.loadDashboardCounts(); },
      error: () => {}
    });
  }

  rejectWallet(req: WalletReq) {
    this.adminSvc.rejectWallet(req.requestId).subscribe({
      next: () => { this.loadWallets(); this.loadDashboardCounts(); },
      error: () => {}
    });
  }

  // ── Space Approvals ───────────────────────────────────────────────────────
  loadSpaces() {
    this.spacesLoading = true;
    this.adminSvc.getSpaceApprovals().subscribe({
      next: s => { this.spaceApprovals = s; this.spacesLoading = false; },
      error: () => this.spacesLoading = false
    });
  }

  get filteredSpaces(): SpaceApproval[] {
    if (this.spaceFilter === 'ALL') return this.spaceApprovals;
    return this.spaceApprovals.filter(s => s.status === this.spaceFilter);
  }

  approveSpace(s: SpaceApproval) {
    this.adminSvc.approveSpace(s.approvalId).subscribe({
      next: () => { this.loadSpaces(); this.loadDashboardCounts(); },
      error: () => {}
    });
  }

  openReject(s: SpaceApproval) {
    this.rejectSpaceId = s.approvalId;
    this.rejectReason = '';
    this.showRejectModal = true;
  }

  confirmRejectSpace() {
    if (!this.rejectSpaceId) return;
    this.adminSvc.rejectSpace(this.rejectSpaceId, this.rejectReason).subscribe({
      next: () => { this.showRejectModal = false; this.loadSpaces(); this.loadDashboardCounts(); },
      error: () => {}
    });
  }

  facilitiesOf(s: SpaceApproval): string[] {
    const f: string[] = [];
    if (s.cctv) f.push('CCTV');
    if (s.ev) f.push('EV');
    if (s.guarded) f.push('Guarded');
    if (s.covered) f.push('Covered');
    return f;
  }

  // ── Promos ────────────────────────────────────────────────────────────────
  loadPromos() {
    this.promosLoading = true;
    this.adminSvc.getPromos().subscribe({
      next: p => { this.promos = p; this.promosLoading = false; },
      error: () => this.promosLoading = false
    });
  }

  validatePromoForm(): string {
    const f = this.promoForm;
    if (!f.code || !f.code.trim()) return 'Code is required';
    if (!/^[A-Z0-9]{3,20}$/.test(f.code.trim())) return 'Code must be 3–20 uppercase letters/numbers';
    if (f.discountPercentage === null || f.discountPercentage === '') return 'Discount is required';
    if (f.discountPercentage < 1 || f.discountPercentage > 100) return 'Discount must be between 1 and 100';
    if (!f.expiryDate) return 'Expiry date is required';
    if (new Date(f.expiryDate) <= new Date()) return 'Expiry date must be in the future';
    return '';
  }

  submitPromo() {
    const err = this.validatePromoForm();
    if (err) { this.promoFormError = err; return; }
    this.promoFormLoading = true; this.promoFormError = '';
    const payload = { ...this.promoForm, code: this.promoForm.code.toUpperCase(), expiryDate: new Date(this.promoForm.expiryDate).toISOString() };
    this.adminSvc.createPromo(payload).subscribe({
      next: () => { this.promoFormLoading = false; this.showPromoForm = false; this.promoForm = { code: '', discountPercentage: null, expiryDate: '' }; this.loadPromos(); },
      error: err => { this.promoFormLoading = false; this.promoFormError = err?.error || 'Failed to create'; }
    });
  }

  togglePromo(p: PromoDTO) {
    this.adminSvc.togglePromo(p.promoId).subscribe({ next: () => this.loadPromos(), error: () => {} });
  }

  confirmDeletePromo() {
    if (!this.deletePromoConfirm) return;
    this.adminSvc.deletePromo(this.deletePromoConfirm.promoId).subscribe({
      next: () => { this.deletePromoConfirm = null; this.loadPromos(); },
      error: () => {}
    });
  }

  // ── Profile ───────────────────────────────────────────────────────────────
  toggleProfile(e: Event) { e.stopPropagation(); this.profileOpen = !this.profileOpen; }

  logout() {
    this.authSvc.logout().subscribe(() => this.router.navigate(['/']));
  }

  formatDt(dt: string): string {
    if (!dt) return '—';
    return new Date(dt).toLocaleString('en-IN', { dateStyle: 'short', timeStyle: 'short' });
  }

  formatDate(dt: string): string {
    if (!dt) return '—';
    return new Date(dt).toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
