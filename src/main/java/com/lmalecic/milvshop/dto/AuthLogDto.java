package com.lmalecic.milvshop.dto;

import java.time.LocalDateTime;

public record AuthLogDto(
        Long id,
        LocalDateTime timestamp,
        String username,
        String ipAddress
) {}
