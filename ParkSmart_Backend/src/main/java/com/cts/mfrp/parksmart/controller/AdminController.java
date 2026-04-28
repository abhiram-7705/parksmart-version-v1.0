package com.cts.mfrp.parksmart.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.cts.mfrp.parksmart.dto.*;
import com.cts.mfrp.parksmart.repository.UserRepository;
import com.cts.mfrp.parksmart.service.AdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private UserRepository userRepository;

    private void requireAdmin(Authentication auth) {
        userRepository.findByEmail(auth.getName())
            .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
            .orElseThrow(() -> new RuntimeException("Access denied"));
    }

    @PostMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboard(Authentication auth) {
        requireAdmin(auth);
        return ResponseEntity.ok(adminService.getDashboard());
    }

    // ── Wallet Requests ──
    @PostMapping("/wallet/requests")
    public ResponseEntity<List<WalletRequestDTO>> getWalletRequests(Authentication auth) {
        requireAdmin(auth);
        return ResponseEntity.ok(adminService.getAllWalletRequests());
    }

    @PostMapping("/wallet/approve")
    public ResponseEntity<String> approveWallet(@RequestBody AdminActionDTO dto, Authentication auth) {
        requireAdmin(auth);
        adminService.approveWalletRequest(dto.getId());
        return ResponseEntity.ok("Approved");
    }

    @PostMapping("/wallet/reject")
    public ResponseEntity<String> rejectWallet(@RequestBody AdminActionDTO dto, Authentication auth) {
        requireAdmin(auth);
        adminService.rejectWalletRequest(dto.getId());
        return ResponseEntity.ok("Rejected");
    }

    // ── Promo Codes ──
    @PostMapping("/promos")
    public ResponseEntity<List<PromoCodeDTO>> getPromos(Authentication auth) {
        requireAdmin(auth);
        return ResponseEntity.ok(adminService.getAllPromoCodes());
    }

    @PostMapping("/promos/create")
    public ResponseEntity<PromoCodeDTO> createPromo(@Valid @RequestBody PromoCodeCreateDTO dto, Authentication auth) {
        requireAdmin(auth);
        return ResponseEntity.ok(adminService.createPromoCode(dto));
    }

    @PostMapping("/promos/toggle")
    public ResponseEntity<PromoCodeDTO> togglePromo(@RequestBody AdminActionDTO dto, Authentication auth) {
        requireAdmin(auth);
        return ResponseEntity.ok(adminService.togglePromoCode(dto.getId()));
    }

    @PostMapping("/promos/delete")
    public ResponseEntity<String> deletePromo(@RequestBody AdminActionDTO dto, Authentication auth) {
        requireAdmin(auth);
        adminService.deletePromoCode(dto.getId());
        return ResponseEntity.ok("Deleted");
    }

    // ── Space Approvals ──
    @PostMapping("/spaces/approvals")
    public ResponseEntity<List<SpaceApprovalDTO>> getSpaceApprovals(Authentication auth) {
        requireAdmin(auth);
        return ResponseEntity.ok(adminService.getAllSpaceApprovals());
    }

    @PostMapping("/spaces/approve")
    public ResponseEntity<String> approveSpace(@RequestBody AdminActionDTO dto, Authentication auth) {
        requireAdmin(auth);
        adminService.approveSpace(dto.getId());
        return ResponseEntity.ok("Space approved");
    }

    @PostMapping("/spaces/reject")
    public ResponseEntity<String> rejectSpace(@RequestBody AdminActionDTO dto, Authentication auth) {
        requireAdmin(auth);
        adminService.rejectSpace(dto.getId(), dto.getReason());
        return ResponseEntity.ok("Space rejected");
    }
}
