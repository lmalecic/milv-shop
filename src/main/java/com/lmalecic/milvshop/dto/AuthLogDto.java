package com.lmalecic.milvshop.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuthLogDto(
        Long id,
        LocalDateTime timestamp,
        String username,
        String ipAddress
) {}
