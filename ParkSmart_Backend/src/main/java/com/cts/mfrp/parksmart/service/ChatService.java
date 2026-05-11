package com.cts.mfrp.parksmart.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatService {

	private static final Map<String, String[]> INTENTS = new LinkedHashMap<>() {
		{
			put("cancel", new String[] { "cancel", "cancell", "stop booking", "remove booking", "delete booking",
					"undo booking" });
			put("extend", new String[] { "extend", "more time", "extra time", "longer", "add time" });
			put("qr", new String[] { "qr", "entry", "access", "get in", "cant get in", "enter", "scan" });
			put("receipt", new String[] { "receipt", "invoice", "bill", "proof", "document" });
			put("book", new String[] { "book", "reserve", "reserv", "how to park", "make a booking", "new booking" });
			put("search", new String[] { "search", "find parking", "look for", "locate", "near", "available parking" });
			put("filter", new String[] { "filter", "sort", "ev", "covered", "cctv", "guarded", "amenities" });
			put("wallet", new String[] { "wallet", "balance", "credit", "how much", "money" });
			put("topup", new String[] { "top up", "topup", "add money", "add balance", "recharge", "load" });
			put("refund", new String[] { "refund", "money back", "get back", "return money" });
			put("tax", new String[] { "tax", "18", "gst", "extra charge", "surcharge" });
			put("promo", new String[] { "promo", "coupon", "discount", "offer", "code", "deal" });
			put("payment", new String[] { "pay", "payment", "how to pay", "cost", "price", "fee", "charge" });
			put("host", new String[] { "host", "list", "add space", "rent out", "my space", "earn", "owner" });
			put("approval", new String[] { "approv", "pending", "rejected", "review", "status" });
			put("slot", new String[] { "slot", "grid", "available slot", "choose slot", "pick slot" });
			put("signup", new String[] { "sign up", "register", "create account", "new account", "join" });
			put("login", new String[] { "login", "sign in", "log in", "cant login", "cannot login" });
			put("profile", new String[] { "profile", "account", "my details", "settings", "my info" });
			put("password", new String[] { "password", "forgot", "reset", "change password" });
			put("support", new String[] { "support", "help", "issue", "problem", "contact", "complaint", "wrong" });
			put("directions",
					new String[] { "direction", "navigate", "how to reach", "location", "map", "route", "where is" });
			put("dashboard", new String[] { "dashboard", "my bookings", "booking list", "all bookings" });
			put("admin", new String[] { "admin", "console", "manage" });
			put("welcome", new String[] { "welcome", "free credit", "signup bonus", "first time", "new user" });
			put("about", new String[]{"parksmart", "what is", "about", "tell me", "explain", "overview", "how does it work", "what can", "what do"});
		}
	};

	private static final Map<String, String> RESPONSES = new HashMap<>() {
		{
			put("cancel",
					"To cancel a booking, go to Dashboard → find the booking → click Cancel. Refund eligibility depends on the host's policy.");
			put("extend",
					"You can extend an active booking from your Dashboard by clicking the Extend button on the booking card.");
			put("qr",
					"Your QR code is on each booking card in the Dashboard. Tap the QR button and show it at the parking site for entry.");
			put("receipt",
					"Download your receipt from the Dashboard by clicking the Receipt button on any completed booking.");
			put("book",
					"To book: Search a location → Select a space → Choose date and time → Pick a slot → Confirm and pay from your wallet.");
			put("search",
					"Use the Search page — enter your location, date, and time. Use filters to narrow down results by type, price, and amenities.");
			put("filter",
					"Filter by type (Public/Private), price range, amenities (EV, Covered, CCTV, Guarded), and sort by Reviews, Price, or Availability.");
			put("wallet",
					"Your wallet balance is shown on your Profile page. You received a welcome credit on signup and can top up anytime.");
			put("topup",
					"Go to Profile → Add Balance. Your top-up request will be reviewed and approved by the admin.");
			put("refund",
					"Refunds go back to your wallet after cancellation, subject to the host's refund policy shown on the booking details.");
			put("tax",
					"An 18% tax is applied on the base parking charge, shown as a line item in the Reservation Summary before you confirm.");
			put("promo",
					"Enter a promo code on the Reservation Summary page before confirming. Codes are uppercase (e.g. SAVE20) and have an expiry date.");
			put("payment",
					"Payments are made from your ParkSmart wallet. Ensure sufficient balance before booking. Top up from the Profile page if needed.");
			put("host",
					"Go to Dashboard → Owner Dashboard → Add Space. Fill in the details and submit. Your space goes live after admin approval.");
			put("approval",
					"Submitted spaces go through admin review before going live. You will be notified once approved or rejected.");
			put("slot",
					"The slot grid shows available (green), selected (blue), and booked (grey) slots. Pick an available slot to proceed.");
			put("signup",
					"Click Sign Up on the home page, fill in your details, and you will receive a welcome wallet credit on first login.");
			put("login",
					"Click Login on the navigation bar and enter your email and password. Use Forgot Password if needed.");
			put("profile",
					"Your Profile page shows your name, email, phone, wallet balance, and full transaction history.");
			put("password",
					"Click Forgot Password on the Login page, enter your email, and follow the link to reset your password.");
			put("support",
					"For support, use the Contact option from the menu or use the Contact button on your booking card to reach the host directly.");
			put("directions",
					"Click the Directions button on your booking card in the Dashboard to get navigation directions to the parking spot.");
			put("dashboard",
					"Your Dashboard shows Active, Upcoming, Past, and Cancelled bookings. You can search, filter, and manage all bookings from here.");
			put("admin", "The Admin Console handles wallet approvals, space approvals, and promo code management.");
			put("welcome",
					"New users receive a welcome wallet credit on their first login, usable directly for parking bookings.");
			put("about", "ParkSmart is a parking booking platform that connects drivers with parking space owners. You can search for parking by location and time, book slots, pay via wallet, and manage all your bookings from the dashboard. Space owners can also list their parking spaces and earn money. How can I help you today?");
		}
	};

	private static final List<String> SUGGESTIONS = List.of("booking", "cancel", "wallet", "promo code", "extend",
			"QR code", "host a space", "payment");

	public String chat(String userMessage) {
		
		if (userMessage.length() < 4) {
		    return "Hi! I'm the ParkSmart assistant. Ask me anything about bookings, wallet, payments, hosting a space, or how ParkSmart works!";
		}
		String message = userMessage.toLowerCase().trim();

		for (Map.Entry<String, String[]> intent : INTENTS.entrySet()) {
			for (String keyword : intent.getValue()) {
				if (message.contains(keyword)) {
					return RESPONSES.get(intent.getKey());
				}
			}
		}

		return "I'm not sure about that. I can help you with: " + String.join(", ", SUGGESTIONS)
				+ ". Try asking about one of these!";
	}
}