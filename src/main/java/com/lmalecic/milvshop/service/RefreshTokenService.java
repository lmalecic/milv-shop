package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.entity.RefreshToken;
import com.lmalecic.milvshop.exception.NoContentException;
import com.lmalecic.milvshop.exception.TokenExpiredException;
import com.lmalecic.milvshop.repository.RefreshTokenRepository;
import com.lmalecic.milvshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiry; // 10 mins
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        Optional<RefreshToken> existingToken = this.refreshTokenRepository.findByUser_Username(username);
        existingToken.ifPresent(token -> {
            this.refreshTokenRepository.deleteByToken(token.getToken());
            this.refreshTokenRepository.flush();
        });

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(this.refreshTokenExpiry))
                .user(this.userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found!")))
                .build();

        return this.refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String token) {
        if (!this.refreshTokenRepository.existsByToken(token)) {
            throw new NoContentException("Refresh token not found.");
        }
        this.refreshTokenRepository.deleteByToken(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiredException("Refresh token expired, please log in again.");
        }
        return refreshToken;
    }
}
