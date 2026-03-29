package com.lmalecic.milvshop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/tanks/**", "/css/**", "/img/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .successHandler(this::handleLoginSuccess)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(this::handleLogoutSuccess));

        return http.build();
    }

    private void handleLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                                    org.springframework.security.core.Authentication authentication) throws IOException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (savedRequest != null) {
            response.sendRedirect(savedRequest.getRedirectUrl());
            return;
        }

        String referer = request.getHeader("Referer");
        if (StringUtils.hasText(referer) && !referer.contains("/login")) {
            response.sendRedirect(referer);
            return;
        }

        response.sendRedirect("/");
    }

    private void handleLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                     org.springframework.security.core.Authentication authentication) throws IOException {
        String referer = request.getHeader("Referer");
        if (StringUtils.hasText(referer) && !referer.contains("/logout")) {
            response.sendRedirect(referer);
            return;
        }
        response.sendRedirect("/");
    }
}
