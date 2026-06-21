package com.lmalecic.milvshop.filter;

import com.lmalecic.milvshop.controller.mvc.AuthMvcController;
import com.lmalecic.milvshop.dto.AuthLogDto;
import com.lmalecic.milvshop.service.AuthLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthAuditFilter extends OncePerRequestFilter {

    private final AuthLogService authLogService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        filterChain.doFilter(request, response);

        if (!AuthMvcController.LOGIN_SUCCESS_URL.equals(uri) || !"POST".equalsIgnoreCase(request.getMethod())) {
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            this.authLogService.log(new AuthLogDto(null, LocalDateTime.now(), authentication.getName(), ip));
        }
    }
}
