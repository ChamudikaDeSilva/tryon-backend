package com.chamz.tryonbackend.repository;

import com.chamz.tryonbackend.model.RefreshToken;
import com.chamz.tryonbackend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token); // ✅ Add this line

    void deleteByToken(String token);
}
