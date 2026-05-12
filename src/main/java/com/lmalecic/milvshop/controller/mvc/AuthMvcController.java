package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.model.User;
import com.lmalecic.milvshop.repository.UserRepository;
import com.lmalecic.milvshop.repository.UserRoleRepository;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponseHeader;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthMvcController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;

    @GetMapping("/login")
    public String getLoginForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "fragments/auth/login-form";
    }

    @HxRequest
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "fragments/auth/login-form";
        }

        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "refresh:htmx";
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.auth", "");
            bindingResult.rejectValue("password", "error.auth", "Invalid username or password");
            return "fragments/auth/login-form";
        }
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "fragments/auth/login-form";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "fragments/auth/login-form";
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username.exists", "Username already exists");
            return "fragments/auth/login-form";
        }

        userRepository.save(User.builder()
                .username(userDto.getUsername())
                .pwdHash(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(userRoleRepository.findByName("USER").orElseThrow())).build());

        return "redirect:/";
    }
}

