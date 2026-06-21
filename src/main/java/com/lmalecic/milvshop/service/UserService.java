package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.UserAuthDto;
import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.model.User;
import com.lmalecic.milvshop.repository.UserRepository;
import com.lmalecic.milvshop.repository.UserRoleRepository;
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

    public UserDto register(UserAuthDto userAuthDto) {
        return this.toDto(this.userRepository.save(User.builder()
                .username(userAuthDto.getUsername())
                .pwdHash(this.passwordEncoder.encode(userAuthDto.getPassword()))
                .roles(List.of(this.userRoleRepository.findByName("ROLE_USER")
                        .orElseThrow()))
                .build()));
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRoles());
    }
}
