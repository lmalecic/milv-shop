package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.AuthLogDto;
import com.lmalecic.milvshop.model.AuthLog;
import com.lmalecic.milvshop.repository.AuthLogRepository;
import com.lmalecic.milvshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthLogService {

    private final AuthLogRepository authLogRepository;
    private final UserRepository userRepository;

    public void log(AuthLogDto authLogDto) {
        this.authLogRepository.save(AuthLog.builder()
                .timestamp(authLogDto.timestamp())
                .user(this.userRepository.findByUsername(authLogDto.username())
                        .orElseThrow())
                .ipAddress(authLogDto.ipAddress())
                .build());
    }

    private AuthLogDto toDto(AuthLog authLog) {
        return new AuthLogDto(authLog.getId(), authLog.getTimestamp(), authLog.getUser().getUsername(), authLog.getIpAddress());
    }
}
