package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.dto.OrderDto;
import com.lmalecic.milvshop.entity.*;
import com.lmalecic.milvshop.service.OrderService;
import com.lmalecic.milvshop.service.UserService;
import com.lmalecic.milvshop.viewmodel.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("checkout")
@SessionAttributes("cart")
@RequiredArgsConstructor
public class CheckoutMvcController {

    private final OrderService orderService;
    private final UserService userService;

    @HxRequest
    @PostMapping
    public String checkout(@RequestParam PaymentType paymentType, @SessionAttribute("cart") Cart cart, @AuthenticationPrincipal User user, SessionStatus sessionStatus, HtmxResponse htmxResponse, RedirectAttributes redirectAttributes) {
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        Order order = this.orderService.create(user, cart, paymentType);

        switch (paymentType) {
            case CASH -> {
                sessionStatus.setComplete();
                return "redirect:htmx:/orders";
            }
            case PAYPAL -> {
                return "";
            }
        }

        return "";
    }
}
