package com.lmalecic.milvshop.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - start;
        int statusCode = response.getStatus();
        String message = "Request from {}: {} {} -> {}, took {}ms";
        Object[] args = { request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), statusCode, duration };

        if (statusCode >= 500) {
            log.error(message, args);
        } else if (statusCode >= 400) {
            log.warn(message, args);
        } else {
            log.info(message, args);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/img/");
    }
}
