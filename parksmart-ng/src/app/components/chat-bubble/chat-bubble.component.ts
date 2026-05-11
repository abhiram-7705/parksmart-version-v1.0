import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ChatService } from '../../services/chat.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-chat-bubble',
  templateUrl: './chat-bubble.component.html',
  styleUrls: ['./chat-bubble.component.css']
})
export class ChatBubbleComponent implements OnInit, AfterViewChecked {
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  isOpen = false;
  isLoading = false;
  inputText = '';
  isVisible = false;
  private shouldScroll = false;

  messages: { role: 'user' | 'bot', text: string }[] = [];

  quickTags = [
    'How to book a slot?',
    'Cancel a booking',
    'Wallet balance',
    'Apply promo code',
    'Host a space',
    'Extend booking'
  ];

  private readonly WELCOME_MESSAGE = 'Hi! 👋 How can I help you with ParkSmart today?';
  private hiddenRoutes = ['/', '/about', '/slots', '/booking', '/booking/confirm'];

  constructor(
    private router: Router,
    private authService: AuthService,
    private chatService: ChatService
  ) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.updateVisibility(event.urlAfterRedirects);
    });

    // Reset chat on logout
    this.authService.loggedIn$.subscribe(loggedIn => {
      if (!loggedIn) {
        this.resetChat();
      }
      this.updateVisibility(this.router.url);
    });

    this.updateVisibility(this.router.url);
    this.resetChat();
  }

  ngAfterViewChecked() {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  private scrollToBottom() {
    try {
      const el = this.scrollContainer.nativeElement;
      el.scrollTop = el.scrollHeight;
    } catch (e) {}
  }

  private updateVisibility(url: string) {
    const isLoggedIn = this.authService.isLoggedIn();
    const isHiddenRoute = this.hiddenRoutes.some(route =>
      route === '/' ? url === '/' : url.startsWith(route)
    );
    this.isVisible = isLoggedIn && !isHiddenRoute;
    if (!this.isVisible) this.isOpen = false;
  }

  resetChat() {
    this.messages = [{ role: 'bot', text: this.WELCOME_MESSAGE }];
    this.inputText = '';
    this.isLoading = false;
  }

  clearMessages() {
    this.resetChat();
    this.shouldScroll = true;
  }

  toggle() {
    this.isOpen = !this.isOpen;
    if (this.isOpen) this.shouldScroll = true;
  }

  sendTag(tag: string) { this.handleSend(tag); }
  send() { this.handleSend(this.inputText); }

  private handleSend(msg: string) {
    msg = msg.trim();
    if (!msg || this.isLoading) return;

    this.messages.push({ role: 'user', text: msg });
    this.inputText = '';
    this.isLoading = true;
    this.shouldScroll = true;

    this.chatService.sendMessage(msg).subscribe({
      next: (reply) => {
        this.messages.push({ role: 'bot', text: reply });
        this.isLoading = false;
        this.shouldScroll = true;
      },
      error: () => {
        this.messages.push({ role: 'bot', text: 'Something went wrong. Please try again.' });
        this.isLoading = false;
        this.shouldScroll = true;
      }
    });
  }

  onKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') this.send();
  }
}