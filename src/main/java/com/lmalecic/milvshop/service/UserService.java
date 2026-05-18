package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.model.User;
import com.lmalecic.milvshop.repository.UserRepository;
import com.lmalecic.milvshop.repository.UserRoleRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserDto userDto) {
        return userRepository.save(User.builder()
                .username(userDto.getUsername())
                .pwdHash(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(userRoleRepository.findByName("ROLE_USER").orElseThrow())).build());
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
