package com.cts.mfrp.parksmart.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import com.cts.mfrp.parksmart.dto.*;
import com.cts.mfrp.parksmart.model.Users;
import com.cts.mfrp.parksmart.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignUpRequestDTO request) {
        userService.registerUser(request);
        return ResponseEntity.ok("user signup suceesful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Users user = userService.login(request);

		Authentication authentication =
		        new UsernamePasswordAuthenticationToken(
		            user.getEmail(),
		            null,
		            Collections.emptyList()
		        );

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		
		HttpSession session = httpRequest.getSession(true);
		    session.setAttribute(
		        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
		        context
		    );


		if (request.isRememberMe()) {
		        Cookie cookie = new Cookie("remember-me", user.getEmail());
		        cookie.setHttpOnly(true);
		        cookie.setMaxAge(7 * 24 * 60 * 60);
		        cookie.setPath("/");
		        httpResponse.addCookie(cookie);
		    }

        return ResponseEntity.ok("user login succesful");
    }

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
	    request.getSession().invalidate();   
	    SecurityContextHolder.clearContext();
		Cookie cookie = new Cookie("remember-me", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	    return ResponseEntity.ok().build();
	}
}