package com.chamz.glamora.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chamz.glamora.auth.dto.AuthResponse;
import com.chamz.glamora.auth.dto.LoginRequest;
import com.chamz.glamora.auth.dto.RegisterRequest;
import com.chamz.glamora.auth.model.RefreshToken;
import com.chamz.glamora.auth.model.Role;
import com.chamz.glamora.auth.model.User;
import com.chamz.glamora.auth.repository.RoleRepository;
import com.chamz.glamora.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.builder().message("Username already exists").build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder().message("Email already exists").build();
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Invalid role"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .role(role)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .message("User registered successfully")
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .message("Login successful")
                .build();
    }

    public AuthResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(token);
        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken.getToken()) // Reuse the same refresh token or generate a new one
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .message("Token refreshed successfully")
                .build();
    }
}
