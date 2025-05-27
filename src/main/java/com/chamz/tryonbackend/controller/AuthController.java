package com.chamz.tryonbackend.controller;

import com.chamz.tryonbackend.dto.RegisterRequest;
import com.chamz.tryonbackend.dto.AuthResponse;
import com.chamz.tryonbackend.dto.LoginRequest;
import com.chamz.tryonbackend.dto.RefreshTokenRequest;
import com.chamz.tryonbackend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
