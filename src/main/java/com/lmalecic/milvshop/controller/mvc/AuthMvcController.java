package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.dto.UserAuthDto;
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

    public static final String LOGIN_SUCCESS_URL = "/auth/login-success";
    public static final String FRAGMENT_AUTH_FORM = "fragments/auth/auth-form";

    private final UserService userService;

    @HxRequest
    @GetMapping("/login")
    public String getLoginForm(Model model, @RequestParam(required = false) String redirect) {
        model.addAttribute("userAuthDto", UserAuthDto.empty());
        model.addAttribute("redirectUrl", redirect);
        return FRAGMENT_AUTH_FORM;
    }

    @HxRequest
    @PostMapping("/login-success")
    public String loginSuccess(@RequestParam(required = false) String redirect) {
        if (redirect != null && !redirect.isBlank()) {
            return "redirect:htmx:" + redirect;
        }
        return "refresh:htmx";
    }

    @HxRequest
    @PostMapping("/login-failed")
    public String loginFailed(@Valid @ModelAttribute UserAuthDto userAuthDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            bindingResult.rejectValue("username", "error.auth", "");
            bindingResult.rejectValue("password", "error.auth", "Invalid username or password");
        }
        return FRAGMENT_AUTH_FORM;
    }

    @HxRequest
    @PostMapping("/register")
    public String processRegister(Model model, @Valid @ModelAttribute UserAuthDto userAuthDto, BindingResult bindingResult, @RequestParam String redirect, HtmxResponse htmxResponse) {
        model.addAttribute("redirectUrl", redirect);
        if (bindingResult.hasErrors()) {
            return FRAGMENT_AUTH_FORM;
        }

        if (this.userService.existsByUsername(userAuthDto.username())) {
            bindingResult.rejectValue("username", "error.username.exists", "Username already exists");
            return FRAGMENT_AUTH_FORM;
        }

        this.userService.register(userAuthDto);

        htmxResponse.addTrigger("login");
        return FRAGMENT_AUTH_FORM;
    }
}

