package com.chamz.glamora.auth.service;

//import com.chamz.tryonbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.chamz.glamora.auth.model.RefreshToken;
import com.chamz.glamora.auth.model.User;
import com.chamz.glamora.auth.repository.RefreshTokenRepository;
import com.chamz.glamora.exception.InvalidCredentialsException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    //private final UserRepository userRepository;

    public RefreshToken createRefreshToken(User user) {
        return refreshTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setToken(UUID.randomUUID().toString());
                    existingToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60)); // 7 days
                    return refreshTokenRepository.save(existingToken);
                })
                .orElseGet(() -> refreshTokenRepository.save(
                        RefreshToken.builder()
                                .user(user)
                                .token(UUID.randomUUID().toString())
                                .expiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                                .build()
                ));
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidCredentialsException("Refresh token expired");
        }

        return refreshToken;
    }

    
}
