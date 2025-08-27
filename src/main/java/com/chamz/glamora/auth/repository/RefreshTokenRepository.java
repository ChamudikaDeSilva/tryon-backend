package com.chamz.glamora.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chamz.glamora.auth.model.RefreshToken;
import com.chamz.glamora.auth.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token); // ✅ Add this line

    void deleteByToken(String token);
}
