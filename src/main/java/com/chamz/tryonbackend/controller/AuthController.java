package com.chamz.tryonbackend.controller;

import com.chamz.tryonbackend.dto.RegisterRequest;
import com.chamz.tryonbackend.dto.AuthResponse;
import com.chamz.tryonbackend.dto.LoginRequest;
import com.chamz.tryonbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        logger.info("Received register request for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        logger.info("Register successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("Received login request for email: {}", request.getEmail());
        AuthResponse response = authService.authenticate(request);
        logger.info("Login successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}
