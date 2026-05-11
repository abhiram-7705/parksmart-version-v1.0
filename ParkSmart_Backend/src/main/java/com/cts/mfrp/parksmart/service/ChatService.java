package com.cts.mfrp.parksmart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

	@Value("${groq.api.key}")
	private String apiKey;

	@Value("${groq.api.url}")
	private String apiUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	private static final String SYSTEM_PROMPT = """
			You are a helpful assistant for ParkSmart, a parking booking platform.
			Answer only questions related to ParkSmart and parking.
			Keep answers short, friendly, and clear.

			Here is what you know about ParkSmart:
			- Users can search for parking by location, date, and time
			- Filters: parking type (public/private), price range, amenities (EV, Covered, CCTV, Guarded)
			- Users can book a slot, view a slot grid, and get a QR code for entry
			- Wallet system is used for payments. Users get a welcome credit on signup
			- Wallet top-ups require admin approval
			- 18% tax is applied on all bookings
			- Promo codes can be applied at checkout (uppercase, e.g. SAVE20)
			- Bookings can be cancelled from the dashboard
			- Users can extend an active booking from the dashboard
			- Hosts can list parking spaces — submitted spaces need admin approval before going live
			- Admin can manage wallets, approve spaces, and control promo codes

			If asked anything unrelated to parking or ParkSmart, politely say you can only
			help with ParkSmart related questions.
			""";

	@SuppressWarnings("rawtypes")
	public String chat(String userMessage) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> body = Map.of("model", "llama-3.3-70b-versatile", "messages", List.of(
				Map.of("role", "system", "content", SYSTEM_PROMPT), Map.of("role", "user", "content", userMessage)));

		ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(body, headers),
				Map.class);

		List choices = (List) response.getBody().get("choices");
		Map first = (Map) choices.get(0);
		Map message = (Map) first.get("message");
		return (String) message.get("content");
	}
}