import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { CommonModule, DecimalPipe } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { LoginModalComponent } from './components/login-modal/login-modal.component';
import { SignupModalComponent } from './components/signup-modal/signup-modal.component';
import { AboutComponent } from './components/about/about.component';
import { SearchComponent } from './components/search/search.component';
import { SlotSelectionComponent } from './components/slot-selection/slot-selection.component';
import { ReservationSummaryComponent } from './components/reservation-summary/reservation-summary.component';
import { BookingConfirmationComponent } from './components/booking-confirmation/booking-confirmation.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SafePipe } from './components/shared/safe.pipe';

import { AuthInterceptor } from './services/auth.interceptor';

import { BaseUrlInterceptor } from './interceptor/base-url.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    LoginModalComponent,
    SignupModalComponent,
    AboutComponent,
    SearchComponent,
    SlotSelectionComponent,
    ReservationSummaryComponent,
    BookingConfirmationComponent,
    DashboardComponent,
    ProfileComponent,
    SafePipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    AppRoutingModule
  ],
  providers: [
    DecimalPipe,
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
