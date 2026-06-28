package com.lmalecic.milvshop.dto;

public record JwtResponseDto(
        String accessToken,
        String refreshToken
) {}
