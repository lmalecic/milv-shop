package com.lmalecic.milvshop.config;

import com.lmalecic.milvshop.filter.AuthAuditFilter;
import io.github.wimdeblauwe.htmx.spring.boot.security.HxRefreshHeaderAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new Argon2Password4jPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthAuditFilter authAuditFilter) throws Exception {
        var entryPoint = new HxRefreshHeaderAuthenticationEntryPoint();
        var requestMatcher = new RequestHeaderRequestMatcher("HX-Request");
        http.addFilterAfter(authAuditFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                        .requestMatchers("/error", "/login/**", "/", "/tanks/**", "/cart/**").permitAll()
                        .requestMatchers("/api/format/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/?login")
                        .loginProcessingUrl("/login")
                        .successForwardUrl("/auth/login-success")
                        .failureForwardUrl("/auth/login-failed")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .exceptionHandling(configurer -> configurer
                        .defaultAuthenticationEntryPointFor(entryPoint, requestMatcher));

        return http.build();
    }
}
