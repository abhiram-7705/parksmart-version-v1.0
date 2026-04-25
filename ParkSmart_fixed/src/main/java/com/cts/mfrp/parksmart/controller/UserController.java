package com.cts.mfrp.parksmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cts.mfrp.parksmart.dto.UserProfileDTO;
import com.cts.mfrp.parksmart.dto.WalletDTO;
import com.cts.mfrp.parksmart.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

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
}
