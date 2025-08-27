package com.chamz.glamora.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chamz.glamora.auth.dto.AuthResponse;
import com.chamz.glamora.auth.dto.LoginRequest;
import com.chamz.glamora.auth.dto.RefreshTokenRequest;
import com.chamz.glamora.auth.dto.RegisterRequest;
import com.chamz.glamora.auth.service.AuthService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if(request.getRole()==null || request.getRole().isBlank() || request.getRole().isEmpty()){
            request.setRole("CUSTOMER");
        }
        // logger.info("Received register request for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        // logger.info("Register successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // logger.info("Received login request for email: {}", request.getEmail());
        AuthResponse response = authService.authenticate(request);
        // logger.info("Login successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

}
