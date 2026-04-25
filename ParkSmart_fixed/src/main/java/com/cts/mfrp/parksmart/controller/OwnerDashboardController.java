package com.cts.mfrp.parksmart.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cts.mfrp.parksmart.dto.*;
import com.cts.mfrp.parksmart.service.OwnerDashboardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/owner-dashboard")
@CrossOrigin
public class OwnerDashboardController {

    @Autowired
    private OwnerDashboardService ownerDashboardService;

    /** GET all spaces for the logged-in owner */
    @PostMapping("/dashboard/cards")
    public ResponseEntity<List<ParkingCardDTO>> getOwnerSpaces(Authentication auth) {
        return ResponseEntity.ok(ownerDashboardService.getParkingCardsForOwner(auth.getName()));
    }

    /** POST add a new space */
    @PostMapping("/spaces/add")
    public ResponseEntity<ParkingCardDTO> addSpace(
            @Valid @RequestBody AddSpaceRequestDTO req, Authentication auth) {
        return ResponseEntity.ok(ownerDashboardService.addSpace(req, auth.getName()));
    }

    /** PUT edit an existing space */
    @PostMapping("/spaces/edit")
    public ResponseEntity<ParkingCardDTO> editSpace(
            @Valid @RequestBody EditSpaceRequestDTO req, Authentication auth) {
        return ResponseEntity.ok(ownerDashboardService.editSpace(req, auth.getName()));
    }

    /** POST delete a space */
    @PostMapping("/spaces/delete")
    public ResponseEntity<String> deleteSpace(
            @RequestBody SpaceIdRequestDTO req, Authentication auth) {
        ownerDashboardService.deleteSpace(req.getSpaceId(), auth.getName());
        return ResponseEntity.ok("Space deleted successfully");
    }

    /** POST get space detail */
    @PostMapping("/spaces/detail")
    public ResponseEntity<SpaceDetailDTO> getSpaceDetail(
            @RequestBody SpaceIdRequestDTO req, Authentication auth) {
        return ResponseEntity.ok(ownerDashboardService.getSpaceDetail(req.getSpaceId(), auth.getName()));
    }

    /** POST get slots for a space */
    @PostMapping("/slots")
    public ResponseEntity<List<OwnerSlotDTO>> getSlots(
            @RequestBody SpaceIdRequestDTO req, Authentication auth) {
        return ResponseEntity.ok(ownerDashboardService.getSlotsForOwnerSpace(auth.getName(), req.getSpaceId()));
    }

    /** POST add a slot */
    @PostMapping("/slots/add")
    public ResponseEntity<OwnerSlotDTO> addSlot(
            @Valid @RequestBody AddSlotRequestDTO req, Authentication auth) {
        return ResponseEntity.ok(ownerDashboardService.addSlot(req, auth.getName()));
    }

    /** POST toggle slot status FREE <-> BLOCKED */
    @PostMapping("/slots/toggle-status")
    public ResponseEntity<Void> toggleSlot(
            @RequestBody SlotIdRequestDTO req, Authentication auth) {
        ownerDashboardService.toggleSlotStatus(auth.getName(), req.getSlotId());
        return ResponseEntity.ok().build();
    }

    /** POST delete a slot */
    @PostMapping("/slots/delete")
    public ResponseEntity<String> deleteSlot(
            @RequestBody SlotIdRequestDTO req, Authentication auth) {
        ownerDashboardService.deleteSlot(req.getSlotId(), auth.getName());
        return ResponseEntity.ok("Slot deleted");
    }

    /** POST get bookings for a space with optional status filter */
    @PostMapping("/bookings")
    public ResponseEntity<List<OwnerBookingDTO>> getBookings(
            @RequestBody SpaceBookingFilterDTO req, Authentication auth) {
        return ResponseEntity.ok(
            ownerDashboardService.getBookingsForSpace(req.getSpaceId(), req.getStatus(), auth.getName())
        );
    }

    /** POST get timeline for a space on a date */
    @PostMapping("/timeline")
    public ResponseEntity<List<TimelineSlotDTO>> getTimeline(
            @RequestBody TimelineRequestDTO req, Authentication auth) {
        LocalDate date = req.getDate() != null ? req.getDate() : LocalDate.now();
        return ResponseEntity.ok(
            ownerDashboardService.getTimeline(req.getSpaceId(), date, auth.getName())
        );
    }
}
