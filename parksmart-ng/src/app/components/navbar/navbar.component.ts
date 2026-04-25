import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ModalService } from '../../services/modal.service';
import { UserService, UserProfile } from '../../services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  scrolled = false;
  menuOpen = false;
  profileOpen = false;
  profile: UserProfile | null = null;

  navLinks = [
    { label: 'Home', route: '/', requiresAuth: false },
    { label: 'About', route: '/about', requiresAuth: false },
    { label: 'Find Your Space', route: '/search', requiresAuth: true },
    { label: 'Rent your Space', route: '/dashboard', requiresAuth: true },
    { label: 'Blog', route: null, requiresAuth: true },
    { label: 'Contact', route: null, requiresAuth: true }
  ];

  hamburgerGroups = [
    { section: 'Explore', items: ['Search', 'Bookings Dashboard', 'Host a Space', 'Admin Console'] },
    { section: 'Help', items: ['Pricing', 'Support'] }
  ];

  constructor(
    private auth: AuthService,
    private modal: ModalService,
    private router: Router,
    private userSvc: UserService
  ) {}

  ngOnInit() {
    this.auth.loggedIn$.subscribe(v => {
      this.isLoggedIn = v;
      if (v) this.userSvc.getProfile().subscribe({ next: p => this.profile = p, error: () => {} });
    });
  }

  @HostListener('window:scroll') onScroll() { this.scrolled = window.scrollY > 8; }

  @HostListener('document:click', ['$event']) onDocClick(e: Event) {
    const t = e.target as HTMLElement;
    if (!t.closest('.hamburger-wrap')) this.menuOpen = false;
    if (!t.closest('.profile-wrap')) this.profileOpen = false;
  }

  handleNav(link: any, e: Event) {
    e.preventDefault();
    if (link.requiresAuth && !this.isLoggedIn) { this.modal.openLogin(); return; }
    if (link.route) this.router.navigate([link.route]);
  }

  toggleMenu(e: Event) { e.stopPropagation(); this.menuOpen = !this.menuOpen; }
  toggleProfile(e: Event) { e.stopPropagation(); this.profileOpen = !this.profileOpen; }
  clickHamburgerItem() { this.menuOpen = false; if (!this.isLoggedIn) this.modal.openLogin(); }
  openLogin() { this.modal.openLogin(); }
  goProfile() { this.profileOpen = false; this.router.navigate(['/profile']); }
  goDashboard() { this.profileOpen = false; this.router.navigate(['/dashboard']); }
  logout() { this.profileOpen = false; this.auth.logout().subscribe(() => { this.profile = null; this.router.navigate(['/']); }); }
  isActive(route: string | null): boolean { return !!route && this.router.url.startsWith(route) && route !== '/'; }
  isHome(): boolean { return this.router.url === '/'; }
}
