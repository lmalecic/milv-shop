package com.lmalecic.milvshop.config;

import com.lmalecic.milvshop.model.User;
import com.lmalecic.milvshop.model.UserRole;
import com.lmalecic.milvshop.repository.UserRepository;
import com.lmalecic.milvshop.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        userRepository.save(
                User.builder()
                    .username("admin")
                    .pwdHash(passwordEncoder.encode("password"))
                    .roles(List.of(
                        userRoleRepository.findByName("ADMIN")
                                .orElse(UserRole.builder()
                                        .name("ADMIN")
                                        .build()
                                )
                            )
                    )
                    .build()
        );
    }
}
