package com.lmalecic.milvshop.controller.rest;

import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.exception.UserAlreadyExistsException;
import com.lmalecic.milvshop.model.User;
import com.lmalecic.milvshop.repository.UserRepository;
import com.lmalecic.milvshop.repository.UserRoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("User " + userDto.getUsername() + " already exists!");
        }

        userRepository.save(User.builder()
                .username(userDto.getUsername())
                .pwdHash(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(userRoleRepository.findByName("USER").orElseThrow())).build());
    }
}
