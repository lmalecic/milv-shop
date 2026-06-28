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
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthMvcController {

    public static final String LOGIN_SUCCESS_URL = "/auth/login-success";
    public static final String FRAGMENT_AUTH_FORM = "fragments/auth/auth-form";

    private final UserService userService;

    @HxRequest
    @GetMapping("login")
    public String getLoginForm(Model model, @RequestParam(required = false) String redirectUrl) {
        model.addAttribute("userAuthDto", UserAuthDto.empty());
        model.addAttribute("redirectUrl", redirectUrl);
        return FRAGMENT_AUTH_FORM;
    }

    @HxRequest
    @PostMapping("login-success")
    public String loginSuccess(@RequestParam(required = false) String redirectUrl) {
        if (redirectUrl != null && !redirectUrl.isBlank()) {
            return "redirect:htmx:" + redirectUrl;
        }
        return "refresh:htmx";
    }

    @HxRequest
    @PostMapping("login-failed")
    public String loginFailed(@Valid @ModelAttribute UserAuthDto userAuthDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            bindingResult.rejectValue("username", "error.auth", "");
            bindingResult.rejectValue("password", "error.auth", "Invalid username or password");
        }
        return FRAGMENT_AUTH_FORM;
    }

    @HxRequest
    @PostMapping("register")
    public String processRegister(Model model, @Valid @ModelAttribute UserAuthDto userAuthDto, BindingResult bindingResult, @RequestParam(required = false) String redirectUrl, HtmxResponse htmxResponse) {
        model.addAttribute("redirectUrl", redirectUrl);
        if (!bindingResult.hasErrors()) {
            if (this.userService.existsByUsername(userAuthDto.username())) {
                bindingResult.rejectValue("username", "error.username.exists", "Username already exists");
            } else {
                this.userService.register(userAuthDto);
                htmxResponse.addTrigger("login");
            }
        }

        return FRAGMENT_AUTH_FORM;
    }
}

