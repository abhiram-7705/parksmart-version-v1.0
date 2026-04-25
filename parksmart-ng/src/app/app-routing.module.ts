import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { SearchComponent } from './components/search/search.component';
import { SlotSelectionComponent } from './components/slot-selection/slot-selection.component';
import { ReservationSummaryComponent } from './components/reservation-summary/reservation-summary.component';
import { BookingConfirmationComponent } from './components/booking-confirmation/booking-confirmation.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: 'search', component: SearchComponent, canActivate: [AuthGuard] },
  { path: 'slots/:spaceId', component: SlotSelectionComponent, canActivate: [AuthGuard] },
  { path: 'booking', component: ReservationSummaryComponent, canActivate: [AuthGuard] },
  { path: 'booking/confirm', component: BookingConfirmationComponent, canActivate: [AuthGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'top' })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
