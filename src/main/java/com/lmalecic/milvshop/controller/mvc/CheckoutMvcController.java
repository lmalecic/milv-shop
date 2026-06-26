package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.dto.OrderDto;
import com.lmalecic.milvshop.entity.*;
import com.lmalecic.milvshop.service.OrderService;
import com.lmalecic.milvshop.service.UserService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("checkout")
@RequiredArgsConstructor
public class CheckoutMvcController {

    private final OrderService orderService;
    private final UserService userService;

    @HxRequest
    @PostMapping
    public String checkout(@RequestParam PaymentType paymentType, @SessionAttribute("cart") Cart cart, @AuthenticationPrincipal User user, SessionStatus sessionStatus) {
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        Order order = this.orderService.create(user, cart, paymentType);

        switch (paymentType) {
            case CASH -> {
                sessionStatus.setComplete();
                return "redirect:/";
            }
            case PAYPAL -> {
                return "";
            }
        }

        return "";
    }
}
