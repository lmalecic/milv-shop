package com.lmalecic.milvshop.controller.mvc;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.exception.PaymentException;
import com.lmalecic.milvshop.entity.*;
import com.lmalecic.milvshop.service.paypal.PayPalCheckoutResult;
import com.lmalecic.milvshop.service.paypal.PayPalCheckoutService;
import com.lmalecic.milvshop.service.OrderService;
import com.lmalecic.milvshop.viewmodel.Toast;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("checkout")
@SessionAttributes("cart")
@RequiredArgsConstructor
public class CheckoutMvcController {

    private static final String OPEN_PAYPAL_CHECKOUT_EVENT = "openPayPalCheckout";
    private static final String CLOSE_PAYPAL_CHECKOUT_EVENT = "closePayPalCheckout";
    private static final String PAYPAL_CHECKOUT_FINISHED_VIEW = "paypal-checkout-finished";

    private final OrderService orderService;
    private final PayPalCheckoutService payPalCheckoutService;

    @HxRequest
    @PostMapping
    public String checkout(@RequestParam PaymentType paymentType, @SessionAttribute("cart") Cart cart, @AuthenticationPrincipal User user, SessionStatus sessionStatus, HtmxResponse htmxResponse, HttpServletResponse response) {
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:htmx:/cart";
        }

        if (PaymentType.CASH == paymentType) {
            this.orderService.cancelPendingPayPalOrdersForUser(user);
            this.orderService.create(user, cart, paymentType);
            sessionStatus.setComplete();
            return "redirect:htmx:/orders";
        } else if (PaymentType.PAYPAL == paymentType) {
            if (!this.payPalCheckoutService.isConfigured()) {
                htmxResponse.addTrigger(CLOSE_PAYPAL_CHECKOUT_EVENT);
                htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.error("PayPal checkout is not configured."));
                return "fragments/cart/form";
            }

            this.orderService.cancelPendingPayPalOrdersForUser(user);
            Order order = this.orderService.create(user, cart, paymentType);
            try {
                URI returnUrl = this.payPalReturnUrl(order.getId());
                URI cancelUrl = this.payPalCancelUrl(order.getId());
                PayPalCheckoutResult checkout = this.payPalCheckoutService.createOrder(order, cart, returnUrl, cancelUrl);
                this.orderService.attachPayPalOrderId(order.getId(), checkout.orderId());

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                htmxResponse.addTrigger(OPEN_PAYPAL_CHECKOUT_EVENT, checkout.approvalUrl());
                return null;
            } catch (PaymentException e) {
                this.orderService.setStatus(order.getId(), OrderStatus.CANCELLED);
                htmxResponse.addTrigger(CLOSE_PAYPAL_CHECKOUT_EVENT);
                htmxResponse.addTrigger(Toast.PUSH_TOAST_EVENT, Toast.error(e.getMessage()));
                return "fragments/cart/form";
            }
        }

        return "";
    }

    @GetMapping("paypal/return")
    public String payPalReturn(Model model, @RequestParam Long orderId, @RequestParam("token") String paypalOrderId, @AuthenticationPrincipal User user, SessionStatus sessionStatus) {
        this.orderService.findPendingPayPalOrderForUser(orderId, user.getId(), paypalOrderId);
        this.payPalCheckoutService.captureOrder(orderId, paypalOrderId);
        this.orderService.completePayPalOrder(orderId, user.getId(), paypalOrderId);
        sessionStatus.setComplete();
        model.addAttribute("redirectUrl", "/orders");
        return PAYPAL_CHECKOUT_FINISHED_VIEW;
    }

    @GetMapping("paypal/cancel")
    public String payPalCancel(Model model, @RequestParam Long orderId, @RequestParam("token") String paypalOrderId, @AuthenticationPrincipal User user, SessionStatus sessionStatus) {
        this.orderService.cancelPayPalOrder(orderId, user.getId(), paypalOrderId);
        sessionStatus.setComplete();
        model.addAttribute("redirectUrl", "/orders");
        return PAYPAL_CHECKOUT_FINISHED_VIEW;
    }

    private URI payPalReturnUrl(Long orderId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/checkout/paypal/return")
                .queryParam("orderId", orderId)
                .build()
                .toUri();
    }

    private URI payPalCancelUrl(Long orderId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/checkout/paypal/cancel")
                .queryParam("orderId", orderId)
                .build()
                .toUri();
    }
}
