package com.cts.mfrp.parksmart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.mfrp.parksmart.dto.*;
import com.cts.mfrp.parksmart.model.*;
import com.cts.mfrp.parksmart.repository.*;

import jakarta.transaction.Transactional;

@Service
public class AdminService {

    @Autowired private WalletRequestRepository walletRequestRepository;
    @Autowired private SpaceApprovalRequestRepository spaceApprovalRequestRepository;
    @Autowired private PromoCodeRepository promoCodeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WalletTransactionsRepository walletTransactionsRepository;
    @Autowired private ParkingSpacesRepository parkingSpacesRepository;
    @Autowired private ParkingSlotsRepository parkingSlotsRepository;

    // ── DASHBOARD ──────────────────────────────────────────────────────────
    public AdminDashboardDTO getDashboard() {
        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setPendingWalletRequests(walletRequestRepository.countByStatus("PENDING"));
        dto.setPendingSpaceApprovals(spaceApprovalRequestRepository.countByStatus("PENDING"));
        dto.setActivePromoCodes(promoCodeRepository.findAll().stream().filter(PromoCode::isActive).count());
        return dto;
    }

    // ── WALLET REQUESTS ────────────────────────────────────────────────────
    public List<WalletRequestDTO> getAllWalletRequests() {
        return walletRequestRepository.findAllByOrderByRequestedAtDesc()
                .stream().map(this::mapWalletRequest).collect(Collectors.toList());
    }

    @Transactional
    public void approveWalletRequest(Integer requestId) {
        WalletRequest req = walletRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Request already processed");

        req.setStatus("APPROVED");
        req.setResolvedAt(LocalDateTime.now());
        walletRequestRepository.save(req);

        Users user = req.getUser();
        user.setBalance(user.getBalance() + req.getAmount());
        userRepository.save(user);

        WalletTransactions txn = new WalletTransactions();
        txn.setUser(user);
        txn.setAmount(req.getAmount());
        txn.setTransactionType("CREDIT");
        txn.setDescription("Admin approved wallet top-up");
        txn.setTimestamp(LocalDateTime.now());
        walletTransactionsRepository.save(txn);
    }

    @Transactional
    public void rejectWalletRequest(Integer requestId) {
        WalletRequest req = walletRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Request already processed");

        req.setStatus("REJECTED");
        req.setResolvedAt(LocalDateTime.now());
        walletRequestRepository.save(req);
    }

    // ── PROMO CODES ────────────────────────────────────────────────────────
    public List<PromoCodeDTO> getAllPromoCodes() {
        return promoCodeRepository.findAll().stream().map(this::mapPromo).collect(Collectors.toList());
    }

    @Transactional
    public PromoCodeDTO createPromoCode(PromoCodeCreateDTO dto) {
        if (promoCodeRepository.findByCode(dto.getCode()).isPresent())
            throw new RuntimeException("Promo code already exists");

        PromoCode promo = new PromoCode();
        promo.setCode(dto.getCode().toUpperCase());
        promo.setDiscountPercentage(dto.getDiscountPercentage());
        promo.setExpiryDate(dto.getExpiryDate());
        promo.setActive(true);
        promoCodeRepository.save(promo);
        return mapPromo(promo);
    }

    @Transactional
    public PromoCodeDTO togglePromoCode(Integer promoId) {
        PromoCode promo = promoCodeRepository.findById(promoId)
                .orElseThrow(() -> new RuntimeException("Promo not found"));
        promo.setActive(!promo.isActive());
        promoCodeRepository.save(promo);
        return mapPromo(promo);
    }

    @Transactional
    public void deletePromoCode(Integer promoId) {
        PromoCode promo = promoCodeRepository.findById(promoId)
                .orElseThrow(() -> new RuntimeException("Promo not found"));
        promoCodeRepository.delete(promo);
    }

    // ── SPACE APPROVALS ────────────────────────────────────────────────────
    public List<SpaceApprovalDTO> getAllSpaceApprovals() {
        return spaceApprovalRequestRepository.findAllByOrderBySubmittedAtDesc()
                .stream().map(this::mapSpaceApproval).collect(Collectors.toList());
    }

    @Transactional
    public void approveSpace(Integer approvalId) {
        SpaceApprovalRequest req = spaceApprovalRequestRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));
        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Already processed");

        req.setStatus("APPROVED");
        req.setResolvedAt(LocalDateTime.now());
        spaceApprovalRequestRepository.save(req);

        ParkingSpaces space = req.getSpace();
        space.setActive(true);
        space.setApprovalStatus("APPROVED");
        parkingSpacesRepository.save(space);

        // Upgrade owner role
        Users owner = req.getOwner();
        if (!"OWNER".equals(owner.getRole())) {
            owner.setRole("OWNER");
            userRepository.save(owner);
        }
    }

    @Transactional
    public void rejectSpace(Integer approvalId, String reason) {
        SpaceApprovalRequest req = spaceApprovalRequestRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));
        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Already processed");

        req.setStatus("REJECTED");
        req.setRejectionReason(reason);
        req.setResolvedAt(LocalDateTime.now());
        spaceApprovalRequestRepository.save(req);

        ParkingSpaces space = req.getSpace();
        space.setApprovalStatus("REJECTED");
        space.setActive(false);
        parkingSpacesRepository.save(space);
    }

    // ── GENERATE PUBLIC SLOTS ──────────────────────────────────────────────
    @Transactional
    public void generatePublicSlots(GenerateSlotsRequestDTO dto, String email) {
        ParkingSpaces space = parkingSpacesRepository.findById(dto.getSpaceId())
                .orElseThrow(() -> new RuntimeException("Space not found"));

        if (!space.getOwner().getEmail().equals(email))
            throw new RuntimeException("Unauthorized");

        if (!"PUBLIC".equalsIgnoreCase(space.getType()))
            throw new RuntimeException("Only public spaces support slot generation");

        // Delete existing FREE slots
        List<ParkingSlots> existing = parkingSlotsRepository.findBySpaceId(dto.getSpaceId());
        for (ParkingSlots s : existing) {
            if (!"OCCUPIED".equals(s.getStatus()))
                parkingSlotsRepository.delete(s);
        }

        // Generate L{level}-{number} slots
        for (int level = 1; level <= dto.getLevels(); level++) {
            for (int slot = 1; slot <= dto.getSlotsPerLevel(); slot++) {
                String slotNumber = String.format("L%d-%02d", level, slot);
                ParkingSlots ps = new ParkingSlots();
                ps.setSlotNumber(slotNumber);
                ps.setStatus("FREE");
                ps.setParkingSpace(space);
                parkingSlotsRepository.save(ps);
            }
        }
    }

    // ── MAPPERS ────────────────────────────────────────────────────────────
    private WalletRequestDTO mapWalletRequest(WalletRequest r) {
        WalletRequestDTO dto = new WalletRequestDTO();
        dto.setRequestId(r.getRequestId());
        dto.setUserName(r.getUser().getUserName());
        dto.setEmail(r.getUser().getEmail());
        dto.setAmount(r.getAmount());
        dto.setStatus(r.getStatus());
        dto.setRequestedAt(r.getRequestedAt());
        dto.setResolvedAt(r.getResolvedAt());
        return dto;
    }

    private PromoCodeDTO mapPromo(PromoCode p) {
        PromoCodeDTO dto = new PromoCodeDTO();
        dto.setPromoId(p.getPromoId());
        dto.setCode(p.getCode());
        dto.setDiscountPercentage(p.getDiscountPercentage());
        dto.setActive(p.isActive());
        dto.setExpiryDate(p.getExpiryDate());
        return dto;
    }

    private SpaceApprovalDTO mapSpaceApproval(SpaceApprovalRequest r) {
        SpaceApprovalDTO dto = new SpaceApprovalDTO();
        dto.setApprovalId(r.getApprovalId());
        if (r.getSpace() != null) dto.setSpaceId(r.getSpace().getSpaceId());
        dto.setOwnerName(r.getOwner().getUserName());
        dto.setOwnerEmail(r.getOwner().getEmail());
        dto.setStatus(r.getStatus());
        dto.setRejectionReason(r.getRejectionReason());
        dto.setSubmittedAt(r.getSubmittedAt());
        dto.setResolvedAt(r.getResolvedAt());
        dto.setName(r.getSubmittedName());
        dto.setType(r.getSubmittedType());
        dto.setPrice(r.getSubmittedPrice());
        dto.setLocation(r.getSubmittedLocation());
        dto.setCity(r.getSubmittedCity());
        dto.setLatitude(r.getSubmittedLatitude());
        dto.setLongitude(r.getSubmittedLongitude());
        dto.setCctv(r.isSubmittedCctv());
        dto.setEv(r.isSubmittedEv());
        dto.setGuarded(r.isSubmittedGuarded());
        dto.setCovered(r.isSubmittedCovered());
        dto.setNotes(r.getSubmittedNotes());
        return dto;
    }
}
