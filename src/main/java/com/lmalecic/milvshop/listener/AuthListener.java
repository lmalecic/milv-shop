package com.lmalecic.milvshop.listener;

import com.lmalecic.milvshop.dto.AuthLogDto;
import com.lmalecic.milvshop.service.AuthLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthListener {

    private final AuthLogService authLogService;
    private final HttpServletRequest request;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        var authentication = event.getAuthentication();
        this.authLogService.log(AuthLogDto.builder()
                .timestamp(LocalDateTime.now())
                .username(authentication.getName())
                .ipAddress(this.request.getRemoteAddr())
                .build());
    }
}
