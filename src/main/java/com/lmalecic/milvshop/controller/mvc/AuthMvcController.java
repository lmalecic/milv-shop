package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.service.UserService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthMvcController {

    private final UserService userService;

    @HxRequest
    @GetMapping("/login")
    public String getLoginForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "fragments/auth/login-form";
    }

    @HxRequest
    @PostMapping("/login-success")
    public String loginSuccess() {
        return "refresh:htmx";
    }

    @HxRequest
    @PostMapping("/login-failed")
    public String loginFailed(Model model, @Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            bindingResult.rejectValue("username", "error.auth", "");
            bindingResult.rejectValue("password", "error.auth", "Invalid username or password");
        }
        return "fragments/auth/login-form";
    }

    @HxRequest
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult, HtmxResponse htmxResponse) {
        if (bindingResult.hasErrors()) {
            return "fragments/auth/login-form";
        }

        if (this.userService.existsByUsername(userDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username.exists", "Username already exists");
            return "fragments/auth/login-form";
        }

        this.userService.register(userDto);

        htmxResponse.addTrigger("login");
        return "fragments/auth/login-form";
    }
}

