package com.cts.mfrp.parksmart.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mfrp.parksmart.dto.AddSlotRequestDTO;
import com.cts.mfrp.parksmart.dto.AddSpaceRequestDTO;
import com.cts.mfrp.parksmart.dto.EditSpaceRequestDTO;
import com.cts.mfrp.parksmart.dto.OwnerBookingDTO;
import com.cts.mfrp.parksmart.dto.OwnerSlotDTO;
import com.cts.mfrp.parksmart.dto.ParkingCardDTO;
import com.cts.mfrp.parksmart.dto.SpaceDetailDTO;
import com.cts.mfrp.parksmart.dto.TimelineBookingDTO;
import com.cts.mfrp.parksmart.dto.TimelineSlotDTO;
import com.cts.mfrp.parksmart.model.Bookings;
import com.cts.mfrp.parksmart.model.ParkingSlots;
import com.cts.mfrp.parksmart.model.ParkingSpaces;
import com.cts.mfrp.parksmart.model.Reviews;
import com.cts.mfrp.parksmart.model.Users;
import com.cts.mfrp.parksmart.repository.BookingsRepository;
import com.cts.mfrp.parksmart.repository.ParkingSlotsRepository;
import com.cts.mfrp.parksmart.repository.ParkingSpacesRepository;
import com.cts.mfrp.parksmart.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OwnerDashboardService {

    @Autowired private UserRepository userRepository;
    @Autowired private ParkingSpacesRepository parkingSpaceRepository;
    @Autowired private ParkingSlotsRepository parkingSlotsRepository;
    @Autowired private BookingsRepository bookingsRepository;

    // ─── GET OWNER SPACES ──────────────────────
    public List<ParkingCardDTO> getParkingCardsForOwner(String email) {
        Users owner = getOwner(email);
        List<ParkingSpaces> spaces = parkingSpaceRepository.findByOwnerUserId(owner.getUserId());
        return spaces.stream().map(this::mapSpaceToCard).collect(Collectors.toList());
    }

    // ─── ADD SPACE ────────────────────────────
    @Transactional
    public ParkingCardDTO addSpace(AddSpaceRequestDTO req, String email) {
        Users owner = getOwner(email);

        ParkingSpaces space = new ParkingSpaces();
        space.setName(req.getName().trim());
        space.setType(req.getType().toUpperCase());
        space.setPricePerHour(req.getPricePerHour());
        space.setLocation(req.getLocation().trim());
        space.setCity(req.getCity().trim());
        space.setLatitude(req.getLatitude());
        space.setLongitude(req.getLongitude());
        space.setCctv(req.isCctv());
        space.setEvCharging(req.isEvCharging());
        space.setGuarded(req.isGuarded());
        space.setCoveredFence(req.isCoveredFence());
        space.setNotes(req.getNotes());
        space.setActive(true);
        space.setOwner(owner);

        parkingSpaceRepository.save(space);

        // Upgrade role to OWNER
        if (!"OWNER".equals(owner.getRole())) {
            owner.setRole("OWNER");
            userRepository.save(owner);
        }

        return mapSpaceToCard(space);
    }

    // ─── EDIT SPACE ───────────────────────────
    @Transactional
    public ParkingCardDTO editSpace(EditSpaceRequestDTO req, String email) {
        Users owner = getOwner(email);
        ParkingSpaces space = getOwnedSpace(req.getSpaceId(), owner);
        space.setName(req.getName().trim());
        space.setType(req.getType().toUpperCase());
        space.setPricePerHour(req.getPricePerHour());
        space.setCctv(req.isCctv());
        space.setEvCharging(req.isEvCharging());
        space.setGuarded(req.isGuarded());
        space.setCoveredFence(req.isCoveredFence());
        space.setNotes(req.getNotes());
        parkingSpaceRepository.save(space);
        return mapSpaceToCard(space);
    }

    // ─── DELETE SPACE ────────────────────────
    @Transactional
    public void deleteSpace(Integer spaceId, String email) {
        Users owner = getOwner(email);
        ParkingSpaces space = getOwnedSpace(spaceId, owner);
        if (bookingsRepository.existsActiveOrUpcomingBySpaceId(spaceId)) {
            throw new IllegalStateException("Cannot delete space with active or upcoming bookings");
        }
        parkingSpaceRepository.delete(space);
    }

    // ─── GET SLOTS ───────────────────────────
    public List<OwnerSlotDTO> getSlotsForOwnerSpace(String email, Integer spaceId) {
        Users owner = getOwner(email);
        getOwnedSpace(spaceId, owner);
        List<ParkingSlots> slots = parkingSlotsRepository.findBySpaceId(spaceId);
        return slots.stream().map(slot -> {
            OwnerSlotDTO dto = new OwnerSlotDTO();
            dto.setSlotId(slot.getSlotId());
            dto.setSlotNumber(slot.getSlotNumber());
            String status = slot.getStatus() != null ? slot.getStatus().toUpperCase() : "FREE";
            dto.setStatus(status);
            if ("OCCUPIED".equals(status) && slot.getBookings() != null) {
                slot.getBookings().stream()
                    .filter(b -> isActiveBooking(b))
                    .findFirst()
                    .ifPresent(b -> dto.setVehicleNumber(b.getVehicleNumber()));
            }
            boolean hasActive = bookingsRepository.existsActiveOrUpcomingBySlotId(slot.getSlotId());
            dto.setHasActiveBookings(hasActive);
            return dto;
        }).collect(Collectors.toList());
    }

    // ─── ADD SLOT ────────────────────────────
    @Transactional
    public OwnerSlotDTO addSlot(AddSlotRequestDTO req, String email) {
        Users owner = getOwner(email);
        ParkingSpaces space = getOwnedSpace(req.getSpaceId(), owner);
        List<ParkingSlots> existing = parkingSlotsRepository.findBySpaceId(req.getSpaceId());
        boolean duplicate = existing.stream().anyMatch(s -> s.getSlotNumber().equalsIgnoreCase(req.getSlotNumber()));
        if (duplicate) throw new IllegalArgumentException("Slot number already exists in this space");
        ParkingSlots slot = new ParkingSlots();
        slot.setSlotNumber(req.getSlotNumber().toUpperCase());
        slot.setStatus("FREE");
        slot.setParkingSpace(space);
        parkingSlotsRepository.save(slot);
        OwnerSlotDTO dto = new OwnerSlotDTO();
        dto.setSlotId(slot.getSlotId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setStatus("FREE");
        dto.setHasActiveBookings(false);
        return dto;
    }

    // ─── TOGGLE SLOT ─────────────────────────
    @Transactional
    public void toggleSlotStatus(String email, Integer slotId) {
        Users owner = getOwner(email);
        ParkingSlots slot = parkingSlotsRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
        if (slot.getParkingSpace().getOwner().getUserId() != owner.getUserId())
            throw new IllegalArgumentException("Unauthorized slot access");
        String current = slot.getStatus() != null ? slot.getStatus().toUpperCase() : "FREE";
        if ("OCCUPIED".equals(current)) throw new IllegalStateException("Cannot modify an occupied slot");
        slot.setStatus("FREE".equals(current) ? "BLOCKED" : "FREE");
        parkingSlotsRepository.save(slot);
    }

    // ─── DELETE SLOT ─────────────────────────
    @Transactional
    public void deleteSlot(Integer slotId, String email) {
        Users owner = getOwner(email);
        ParkingSlots slot = parkingSlotsRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
        if (slot.getParkingSpace().getOwner().getUserId() != owner.getUserId())
            throw new IllegalArgumentException("Unauthorized");
        if (bookingsRepository.existsActiveOrUpcomingBySlotId(slotId))
            throw new IllegalStateException("Cannot delete slot with active or upcoming bookings");
        parkingSlotsRepository.delete(slot);
    }

    // ─── GET BOOKINGS ────────────────────────
    public List<OwnerBookingDTO> getBookingsForSpace(Integer spaceId, String statusFilter, String email) {
        Users owner = getOwner(email);
        getOwnedSpace(spaceId, owner);
        return bookingsRepository.findBySpaceId(spaceId).stream()
            .map(this::mapBookingToDTO)
            .filter(dto -> statusFilter == null || statusFilter.isBlank()
                    || "ALL".equalsIgnoreCase(statusFilter)
                    || dto.getStatus().equalsIgnoreCase(statusFilter))
            .collect(Collectors.toList());
    }

    // ─── TIMELINE ────────────────────────────
    public List<TimelineSlotDTO> getTimeline(Integer spaceId, LocalDate date, String email) {
        Users owner = getOwner(email);
        getOwnedSpace(spaceId, owner);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<ParkingSlots> slots = parkingSlotsRepository.findBySpaceId(spaceId);
        List<Bookings> allBookings = bookingsRepository.findBySpaceId(spaceId);
        return slots.stream().map(slot -> {
            TimelineSlotDTO tDto = new TimelineSlotDTO();
            tDto.setSlotNumber(slot.getSlotNumber());
            List<TimelineBookingDTO> entries = allBookings.stream()
                .filter(b -> b.getParkingSlot().getSlotId() == slot.getSlotId())
                .filter(b -> !b.getLeaving().isBefore(start) && !b.getArrival().isAfter(end))
                .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getStatus()))
                .map(b -> {
                    TimelineBookingDTO tb = new TimelineBookingDTO();
                    tb.setVehicleNumber(b.getVehicleNumber());
                    tb.setArrival(b.getArrival());
                    tb.setLeaving(b.getLeaving());
                    tb.setStatus(deriveStatus(b));
                    return tb;
                }).collect(Collectors.toList());
            tDto.setBookings(entries);
            return tDto;
        }).collect(Collectors.toList());
    }

    // ─── GET SPACE DETAIL ────────────────────
    public SpaceDetailDTO getSpaceDetail(Integer spaceId, String email) {
        Users owner = getOwner(email);
        ParkingSpaces space = getOwnedSpace(spaceId, owner);
        SpaceDetailDTO dto = new SpaceDetailDTO();
        dto.setSpaceId(space.getSpaceId());
        dto.setName(space.getName());
        dto.setType(space.getType());
        dto.setLocation(space.getLocation());
        dto.setCity(space.getCity());
        dto.setPricePerHour(space.getPricePerHour());
        dto.setLatitude(space.getLatitude());
        dto.setLongitude(space.getLongitude());
        dto.setNotes(space.getNotes());
        dto.setActive(space.isActive());
        List<String> facilities = new ArrayList<>();
        if (space.isCctv()) facilities.add("CCTV");
        if (space.isEvCharging()) facilities.add("EV Charging");
        if (space.isGuarded()) facilities.add("Guarded");
        if (space.isCoveredFence()) facilities.add("Covered");
        dto.setFacilities(facilities);
        List<Reviews> reviews = space.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            double avg = reviews.stream().mapToDouble(Reviews::getRating).average().orElse(0.0);
            dto.setRating(Math.round(avg * 10.0) / 10.0);
        }
        return dto;
    }

    // ─── HELPERS ─────────────────────────────
    private Users getOwner(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private ParkingSpaces getOwnedSpace(Integer spaceId, Users owner) {
        return parkingSpaceRepository.findBySpaceIdAndOwnerUserId(spaceId, owner.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Space not found or unauthorized"));
    }

    private boolean isActiveBooking(Bookings b) {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(b.getArrival()) && !now.isAfter(b.getLeaving())
                && !"CANCELLED".equalsIgnoreCase(b.getStatus());
    }

    private String deriveStatus(Bookings b) {
        if ("CANCELLED".equalsIgnoreCase(b.getStatus())) return "CANCELLED";
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(b.getArrival())) return "UPCOMING";
        if (now.isAfter(b.getLeaving())) return "PAST";
        return "ACTIVE";
    }

    private OwnerBookingDTO mapBookingToDTO(Bookings b) {
        OwnerBookingDTO dto = new OwnerBookingDTO();
        dto.setBookingId(b.getBookingId());
        dto.setSlotNumber(b.getParkingSlot().getSlotNumber());
        dto.setVehicleNumber(b.getVehicleNumber());
        dto.setArrival(b.getArrival());
        dto.setLeaving(b.getLeaving());
        dto.setBookedAt(b.getBookingTime());
        dto.setAmount(b.getTotalAmount());
        dto.setRefundable(b.isRefundable());
        dto.setStatus(deriveStatus(b));
        return dto;
    }

    private ParkingCardDTO mapSpaceToCard(ParkingSpaces space) {
        ParkingCardDTO dto = new ParkingCardDTO();
        dto.setSpaceId(space.getSpaceId());
        dto.setName(space.getName());
        dto.setType(space.getType());
        dto.setLocation(space.getLocation());
        dto.setCity(space.getCity());
        dto.setPricePerHour(space.getPricePerHour());
        dto.setLatitude(space.getLatitude());
        dto.setLongitude(space.getLongitude());
        dto.setDistance(0.0);
        List<String> facilities = new ArrayList<>();
        if (space.isCctv()) facilities.add("CCTV");
        if (space.isEvCharging()) facilities.add("EV");
        if (space.isGuarded()) facilities.add("Guarded");
        if (space.isCoveredFence()) facilities.add("Covered");
        dto.setFacilities(facilities);
        List<Reviews> reviews = space.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            double avg = reviews.stream().mapToDouble(Reviews::getRating).average().orElse(0.0);
            dto.setRating(Math.round(avg * 10.0) / 10.0);
            dto.setCountRating(reviews.size());
        }
        return dto;
    }
}
