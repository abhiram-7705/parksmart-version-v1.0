package com.cts.mfrp.parksmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.mfrp.parksmart.dto.BookingCardDTO;
import com.cts.mfrp.parksmart.dto.BookingSearchRequestDTO;
import com.cts.mfrp.parksmart.dto.CancelBookingRequestDTO;
import com.cts.mfrp.parksmart.dto.ContactRequestDTO;
import com.cts.mfrp.parksmart.dto.RatingRequestDTO;
import com.cts.mfrp.parksmart.dto.SuggestionRequestDTO;
import com.cts.mfrp.parksmart.dto.ContactRequestResponseDTO;
import com.cts.mfrp.parksmart.repository.ContactRequestsRepository;
import com.cts.mfrp.parksmart.service.ContactService;
import com.cts.mfrp.parksmart.service.DashboardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user-dashboard")
@CrossOrigin
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactRequestsRepository contactRequestsRepository;

	@PostMapping("/bookings/suggestions")
	public ResponseEntity<List<String>> getSuggestions(
	        @RequestBody SuggestionRequestDTO request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    List<String> suggestions =
	            dashboardService.getBookingSuggestions(request.getQuery(), email);

	    return ResponseEntity.ok(suggestions);
	}
	@PostMapping("/bookings/search")
	public ResponseEntity<List<BookingCardDTO>> searchBookings(
	        @Valid @RequestBody BookingSearchRequestDTO request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    List<BookingCardDTO> result =
	            dashboardService.searchUserBookings(request, email);

	    return ResponseEntity.ok(result);
	}
	
	@PostMapping("/bookings/cancel")
	public ResponseEntity<String> cancelBooking(
	        @Valid @RequestBody CancelBookingRequestDTO request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    dashboardService.cancelBooking(request.getBookingId(), email);

	    return ResponseEntity.ok("Booking cancelled successfully");
	}
	@PostMapping("/bookings/rate")
	public ResponseEntity<String> submitRating(
	        @Valid @RequestBody RatingRequestDTO request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    dashboardService.submitRating(request, email);

	    return ResponseEntity.ok("Rating submitted successfully");
	}
	@PostMapping("/contacts/submit")
    public ResponseEntity<String> submitContactRequest(
            @Valid @RequestBody ContactRequestDTO requestDTO,
            Authentication authentication) {
        
        String currentUserEmail = authentication.getName();
        
        contactService.saveContactRequest(requestDTO, currentUserEmail);
        
        return ResponseEntity.ok("Contact request submitted successfully.");
    }

	@PostMapping("/contacts/owner")
	public ResponseEntity<List<ContactRequestResponseDTO>> getOwnerContactRequests(
	        Authentication authentication) {
	    String email = authentication.getName();
	    List<ContactRequestResponseDTO> list = contactRequestsRepository
	            .findByOwnerEmail(email)
	            .stream().map(r -> {
	                ContactRequestResponseDTO d = new ContactRequestResponseDTO();
	                d.setRequestId(r.getRequestId());
	                d.setUserName(r.getUser().getUserName());
	                d.setUserEmail(r.getUser().getEmail());
	                d.setName(r.getName());
	                d.setPhone(r.getPhone());
	                d.setEmail(r.getEmail());
	                d.setMessage(r.getMessage());
	                d.setPreferredContact(r.getPreferredContact());
	                d.setPreferredTime(r.getPreferredTime());
	                d.setRequestedAt(r.getRequestedAt());
	                d.setRead(r.isRead());
	                d.setSpaceId(r.getParkingSpace() != null ? r.getParkingSpace().getSpaceId() : null);
	                d.setSpaceName(r.getParkingSpace() != null ? r.getParkingSpace().getName() : null);
	                return d;
	            }).collect(java.util.stream.Collectors.toList());
	    return ResponseEntity.ok(list);
	}

	@PostMapping("/contacts/unread-count")
	public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
	    long count = contactRequestsRepository.countUnreadByOwnerEmail(authentication.getName());
	    return ResponseEntity.ok(count);
	}

	@PostMapping("/contacts/mark-read")
	public ResponseEntity<String> markRead(
	        @RequestBody java.util.Map<String, Integer> body,
	        Authentication authentication) {
	    Integer requestId = body.get("requestId");
	    contactRequestsRepository.markAsRead(requestId);
	    return ResponseEntity.ok("Marked as read");
	}
}
