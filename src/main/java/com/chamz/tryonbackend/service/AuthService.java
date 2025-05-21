package com.chamz.tryonbackend.service;

import com.chamz.tryonbackend.dto.RegisterRequest;
import com.chamz.tryonbackend.dto.AuthResponse;
import com.chamz.tryonbackend.dto.LoginRequest;
import com.chamz.tryonbackend.model.Role;
import com.chamz.tryonbackend.model.User;
import com.chamz.tryonbackend.repository.RoleRepository;
import com.chamz.tryonbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

    // Check if username or email already exists
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

    // Generate JWT token for the newly registered user
    String jwtToken = jwtService.generateToken(user.getEmail());


    // Return user info + token + success message
    return AuthResponse.builder()
            .token(jwtToken)
            .email(user.getEmail())
            .username(user.getUsername())
            .role(user.getRole().getName())  // assuming getName() returns string role name
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

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .message("Login successful")
                .build();
    }

}
