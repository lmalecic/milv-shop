package com.lmalecic.milvshop.controller.rest;

import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.exception.UserAlreadyExistsException;
import com.lmalecic.milvshop.model.User;
import com.lmalecic.milvshop.model.UserRole;
import com.lmalecic.milvshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid UserDto userDto) {
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User " + userDto.getUsername() + " already exists!");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPwdHash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(UserRole.USER);

        userService.save(user);
    }
}
