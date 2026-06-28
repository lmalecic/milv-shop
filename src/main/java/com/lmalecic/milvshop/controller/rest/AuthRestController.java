package com.lmalecic.milvshop.controller.rest;

import com.lmalecic.milvshop.dto.AuthRequestDto;
import com.lmalecic.milvshop.dto.JwtResponseDto;
import com.lmalecic.milvshop.dto.RefreshTokenRequestDto;
import com.lmalecic.milvshop.entity.RefreshToken;
import com.lmalecic.milvshop.service.JwtService;
import com.lmalecic.milvshop.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("login")
    public JwtResponseDto authenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.username(), authRequestDto.password()));
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials!");
        }

        return new JwtResponseDto(
                this.jwtService.generateToken(authRequestDto.username()),
                this.refreshTokenService.createRefreshToken(authRequestDto.username()).getToken()
        );
    }

    @PostMapping("refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return this.refreshTokenService.findByToken(refreshTokenRequestDto.token())
                .map(this.refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> new JwtResponseDto(
                        this.jwtService.generateToken(user.getUsername()),
                        refreshTokenRequestDto.token()
                ))
                .orElseThrow(() -> new RuntimeException("Refresh Token not found!"));
    }

    @PostMapping("/logout")
    public void logout(@RequestBody JwtResponseDto jwtResponseDto) {
        this.refreshTokenService.deleteRefreshToken(jwtResponseDto.refreshToken());
    }
}
