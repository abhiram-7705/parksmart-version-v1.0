package com.cts.mfrp.parksmart.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mfrp.parksmart.dto.SlotAvailabilityRequestDTO;
import com.cts.mfrp.parksmart.dto.SlotAvailabilityResponseDTO;
import com.cts.mfrp.parksmart.dto.SlotDTO;
import com.cts.mfrp.parksmart.dto.SlotHoldRequestDTO;
import com.cts.mfrp.parksmart.dto.SlotHoldResponseDTO;


import com.cts.mfrp.parksmart.model.ParkingSlots;
import com.cts.mfrp.parksmart.model.ParkingSpaces;
import com.cts.mfrp.parksmart.model.SlotHold;
import com.cts.mfrp.parksmart.model.Users;
import com.cts.mfrp.parksmart.repository.BookingsRepository;
import com.cts.mfrp.parksmart.repository.ParkingSlotsRepository;
import com.cts.mfrp.parksmart.repository.ParkingSpacesRepository;
import com.cts.mfrp.parksmart.repository.SlotHoldRepository;
import com.cts.mfrp.parksmart.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingService {
	
	@Autowired
	private ParkingSlotsRepository parkingSlotsRepository;
	@Autowired
	private ParkingSpacesRepository parkingSpacesRepository;
	@Autowired
	private BookingsRepository bookingsRepository;
	@Autowired
	private SlotHoldRepository slotHoldRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public SlotAvailabilityResponseDTO getSlotAvailability(SlotAvailabilityRequestDTO request, String email) {
		slotHoldRepository.deleteByExpiresAtBefore(LocalDateTime.now());
	    // 1. resolve time
		LocalDateTime arrival = LocalDateTime.parse(request.getArrival());
		LocalDateTime leaving = LocalDateTime.parse(request.getLeaving());
		if (!leaving.isAfter(arrival)) {
		    throw new IllegalArgumentException("Invalid time range");
		}
		LocalDateTime minTime = LocalDateTime.now().plusMinutes(15);
		if (arrival.isBefore(minTime)) {
			System.out.println(arrival);
		    throw new IllegalArgumentException("Arrival must be at least 15 minutes from now");
		}
		
		
	    // 2. fetch slots
		List<ParkingSlots> slots =
	            parkingSlotsRepository.findBySpaceId(request.getSpaceId());
		List<SlotDTO> slotDTOs = new ArrayList<>();
		
		
	    // 3. check availability
		for (ParkingSlots slot : slots) {

			boolean isAvailable = isSlotAvailable(slot.getSlotId(), arrival, leaving, email);
			
			System.out.println(isAvailable);

			String slotStatus = slot.getStatus() != null ? slot.getStatus().toUpperCase() : "FREE";
			boolean blockedByOwner = "BLOCKED".equals(slotStatus);
			SlotDTO dto = new SlotDTO();
			dto.setSlotId(slot.getSlotId());
			dto.setSlotNumber(slot.getSlotNumber());
			dto.setStatus(slotStatus);
			dto.setAvailable(isAvailable && !blockedByOwner);

			slotDTOs.add(dto);
		}
		
		
	    // 4. calculate duration + billing
		long durationMinutes = Duration.between(arrival, leaving).toMinutes();
	    int billedHours = (int) Math.ceil(durationMinutes / 60.0);
	    if (billedHours == 0) {
	        billedHours = 1;
	    }
	    ParkingSpaces space = parkingSpacesRepository
	            .findById(request.getSpaceId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid space"));
	    double totalPrice = billedHours * space.getPricePerHour();
	    
	    
	    // 5. return response
	    SlotAvailabilityResponseDTO response = new SlotAvailabilityResponseDTO();
	    response.setSlots(slotDTOs);
	    response.setDurationMinutes((int) durationMinutes);
	    response.setBilledHours(billedHours);
	    response.setTotalPrice(totalPrice);
	    return response;

	}

	private boolean isSlotAvailable(Integer slotId, LocalDateTime arrival, LocalDateTime leaving, String email) {

	    System.out.println("Checking availability for slot: " + slotId);

	    boolean booked = bookingsRepository
	        .existsByParkingSlotSlotIdAndArrivalLessThanAndLeavingGreaterThan(
	            slotId, leaving, arrival
	        );

	    System.out.println("Booked? " + booked);

	    boolean heldByOthers = slotHoldRepository
	    	    .existsBySlotSlotIdAndArrivalLessThanAndLeavingGreaterThanAndExpiresAtAfterAndUserEmailNot(
	    	        slotId,
	    	        leaving,
	    	        arrival,
	    	        LocalDateTime.now(),
	    	        email
	    	    );

	    	return !heldByOthers && !booked;
	}
	
	@Transactional
	public SlotHoldResponseDTO holdSlots(SlotHoldRequestDTO request, String email) {

			
		slotHoldRepository.deleteByUserEmail(email);
		
	    if (request.getSlotIds() == null || request.getSlotIds().isEmpty()) {
	        throw new IllegalArgumentException("No slots selected");
	    }

	    if (request.getSlotIds().size() > 3) {
	        throw new IllegalArgumentException("Maximum 3 slots allowed");
	    }

	    LocalDateTime arrival = LocalDateTime.parse(request.getArrival());
	    LocalDateTime leaving = LocalDateTime.parse(request.getLeaving());

	    if (!leaving.isAfter(arrival)) {
	        throw new IllegalArgumentException("Invalid time range");
	    }

	    ParkingSpaces space = parkingSpacesRepository
	            .findById(request.getSpaceId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid space"));

	    String holdGroupId = UUID.randomUUID().toString();

	    Users user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    List<SlotHold> holds = slotHoldRepository.findAll();

	    System.out.println("==== CURRENT HOLDS ====");
	    for (SlotHold h : holds) {
	        System.out.println(
	            "Slot: " + h.getSlot().getSlotId() +
	            " | User: " + h.getUser().getEmail() +
	            " | Expires: " + h.getExpiresAt()
	        );
	    }
	    
	    List<ParkingSlots> slots = new ArrayList<>();

	    for (Integer slotId : request.getSlotIds()) {

	        ParkingSlots slot = parkingSlotsRepository.findById(slotId)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid slot"));

	        if (slot.getParkingSpace().getSpaceId() != space.getSpaceId()) {
	            throw new IllegalArgumentException("Slot does not belong to selected space");
	        }

	        System.out.println("---- HOLD CHECK START ----");
	        System.out.println("SlotId: " + slotId);
	        System.out.println("Arrival: " + arrival);
	        System.out.println("Leaving: " + leaving);

	        boolean available = isSlotAvailable(slotId, arrival, leaving, email);

	        System.out.println("Available result: " + available);
	        System.out.println("---- HOLD CHECK END ----");

	        if (!available) {
	            throw new IllegalArgumentException("One or more slots are no longer available");
	        }

	        slots.add(slot);
	    }

	    LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

	    for (ParkingSlots slot : slots) {

	        SlotHold hold = new SlotHold();
	        hold.setHoldGroupId(holdGroupId);
	        hold.setSlot(slot);
	        hold.setUser(user);
	        hold.setArrival(arrival);
	        hold.setLeaving(leaving);
	        hold.setExpiresAt(expiry);
	        hold.setCreatedAt(LocalDateTime.now());

	        slotHoldRepository.save(hold);
	    }

	    return new SlotHoldResponseDTO(holdGroupId, expiry);
	}
	
	@Transactional
	public void releaseSlots(String holdId, String email) {

	    if (holdId == null || holdId.isEmpty()) {
	        return;
	    }

	    slotHoldRepository.deleteByHoldGroupIdAndUserEmail(holdId, email);
	}
}
