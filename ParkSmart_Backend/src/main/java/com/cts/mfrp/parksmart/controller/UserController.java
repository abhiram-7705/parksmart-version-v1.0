package com.cts.mfrp.parksmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cts.mfrp.parksmart.dto.UserProfileDTO;
import com.cts.mfrp.parksmart.dto.WalletRequestCreateDTO;
import com.cts.mfrp.parksmart.dto.WalletRequestDTO;
import com.cts.mfrp.parksmart.model.WalletRequest;
import com.cts.mfrp.parksmart.repository.WalletRequestRepository;
import com.cts.mfrp.parksmart.repository.UserRepository;
import com.cts.mfrp.parksmart.model.Users;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import com.cts.mfrp.parksmart.dto.WalletDTO;
import com.cts.mfrp.parksmart.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private WalletRequestRepository walletRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getUserProfile(email));
    }

    @GetMapping("/wallet")
    public ResponseEntity<List<WalletDTO>> getWallet(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getWalletDetails(email));
    }

    @PostMapping("/wallet/request")
    public ResponseEntity<String> raiseWalletRequest(
            @Valid @RequestBody WalletRequestCreateDTO dto,
            Authentication authentication) {
        String email = authentication.getName();
        // Check no pending request
        if (walletRequestRepository.findByUserEmailAndStatus(email, "PENDING").isPresent()) {
            throw new RuntimeException("You already have a pending wallet request");
        }
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WalletRequest req = new WalletRequest();
        req.setUser(user);
        req.setAmount(dto.getAmount());
        req.setStatus("PENDING");
        walletRequestRepository.save(req);
        return ResponseEntity.ok("Request submitted");
    }

    @GetMapping("/wallet/requests")
    public ResponseEntity<List<WalletRequestDTO>> getMyWalletRequests(Authentication authentication) {
        String email = authentication.getName();
        List<WalletRequestDTO> list = walletRequestRepository
                .findByUserEmailOrderByRequestedAtDesc(email)
                .stream().map(r -> {
                    WalletRequestDTO d = new WalletRequestDTO();
                    d.setRequestId(r.getRequestId());
                    d.setAmount(r.getAmount());
                    d.setStatus(r.getStatus());
                    d.setRequestedAt(r.getRequestedAt());
                    d.setResolvedAt(r.getResolvedAt());
                    return d;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
