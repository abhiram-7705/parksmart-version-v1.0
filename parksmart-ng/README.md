# ParkSmart Frontend (Angular 17)

## Prerequisites
- Node.js >= 18
- Angular CLI: `npm install -g @angular/cli@17`

## Setup
```bash
npm install
npm start
```
Open http://localhost:4200

## Project Structure
```
src/app/
├── components/
│   ├── navbar/             # Sticky navbar with auth
│   ├── home/               # Landing page with search card
│   ├── login-modal/        # Login with validation + forgot password
│   ├── signup-modal/       # Registration with validation
│   ├── about/              # About ParkSmart page
│   ├── search/             # Search results with filters + map
│   ├── slot-selection/     # Slot grid with hold timer
│   ├── reservation-summary/# Booking summary + promo + confirm
│   ├── booking-confirmation/# Processing + Invoice
│   ├── dashboard/          # User bookings + Owner spaces (toggled)
│   ├── profile/            # User profile + wallet transactions
│   └── shared/safe.pipe.ts # DomSanitizer pipe for iframes
├── services/               # All HTTP services
└── guards/auth.guard.ts    # Session-based auth guard
```

## Backend URL
Proxied via proxy.conf.json: `/api` → `http://localhost:8081`

## Key Features
- Session-based auth (withCredentials)
- Role-based UI (USER / OWNER)
- 5-min hold timer with visual countdown
- Google Maps embeds for directions + location preview
- Promo code support
- Owner Dashboard: CRUD spaces, slots, bookings, Gantt-style timeline
- Wallet with transaction history
- Mobile-responsive layout
