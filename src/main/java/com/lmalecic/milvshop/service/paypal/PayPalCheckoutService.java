package com.lmalecic.milvshop.service.paypal;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.config.PayPalProperties;
import com.lmalecic.milvshop.entity.Order;
import com.lmalecic.milvshop.exception.PaymentException;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.OrderCaptureRequest;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalCheckoutService {

    private static final String CAPTURE_INTENT = "CAPTURE";
    private static final String APPROVE_LINK_REL = "approve";
    private static final String PAYPAL_REQUEST_ID_HEADER = "PayPal-Request-Id";
    private static final String CREATE_REQUEST_ID_PREFIX = "milv-shop-paypal-create-order-";
    private static final String CAPTURE_REQUEST_ID_PREFIX = "milv-shop-paypal-capture-order-";
    private static final int PAYPAL_SCALE = 2;

    private final PayPalProperties properties;

    public boolean isConfigured() {
        return this.properties.isConfigured();
    }

    public PayPalCheckoutResult createOrder(Order order, Cart cart, URI returnUrl, URI cancelUrl) {
        this.assertConfigured();

        OrderRequest orderRequest = new OrderRequest()
                .checkoutPaymentIntent(CAPTURE_INTENT)
                .applicationContext(new ApplicationContext()
                        .brandName(this.properties.getBrandName())
                        .returnUrl(returnUrl.toString())
                        .cancelUrl(cancelUrl.toString())
                        .userAction("PAY_NOW"))
                .purchaseUnits(List.of(new PurchaseUnitRequest()
                        .referenceId(order.getId().toString())
                        .customId(order.getId().toString())
                        .amountWithBreakdown(new AmountWithBreakdown()
                                .currencyCode(this.properties.getCurrency())
                                .value(cart.getTotal()
                                        .setScale(PAYPAL_SCALE, RoundingMode.HALF_UP)
                                        .toPlainString()))));

        OrdersCreateRequest request = new OrdersCreateRequest()
                .prefer("return=representation")
                .requestBody(orderRequest);
        request.header(PAYPAL_REQUEST_ID_HEADER, CREATE_REQUEST_ID_PREFIX + order.getId());

        try {
            HttpResponse<com.paypal.orders.Order> response = this.client().execute(request);
            com.paypal.orders.Order paypalOrder = response.result();
            return new PayPalCheckoutResult(paypalOrder.id(), this.getApprovalUrl(paypalOrder));
        } catch (IOException e) {
            throw new PaymentException("Unable to create PayPal checkout.", e);
        }
    }

    public void captureOrder(Long orderId, String paypalOrderId) {
        this.assertConfigured();

        OrdersCaptureRequest request = new OrdersCaptureRequest(paypalOrderId)
                .payPalRequestId(CAPTURE_REQUEST_ID_PREFIX + orderId)
                .requestBody(new OrderCaptureRequest());

        try {
            HttpResponse<com.paypal.orders.Order> response = this.client().execute(request);
            if (!"COMPLETED".equalsIgnoreCase(response.result().status())) {
                throw new PaymentException("PayPal payment was not completed.");
            }
        } catch (IOException e) {
            throw new PaymentException("Unable to capture PayPal checkout.", e);
        }
    }

    private PayPalHttpClient client() {
        PayPalEnvironment environment = switch (this.properties.getMode()) {
            case LIVE -> new PayPalEnvironment.Live(this.properties.getClientId(), this.properties.getClientSecret());
            case SANDBOX -> new PayPalEnvironment.Sandbox(this.properties.getClientId(), this.properties.getClientSecret());
        };

        return new PayPalHttpClient(environment);
    }

    private String getApprovalUrl(com.paypal.orders.Order paypalOrder) {
        return paypalOrder.links().stream()
                .filter(link -> APPROVE_LINK_REL.equalsIgnoreCase(link.rel()))
                .map(LinkDescription::href)
                .findFirst()
                .orElseThrow(() -> new PaymentException("PayPal did not return an approval URL."));
    }

    private void assertConfigured() {
        if (!this.properties.isConfigured()) {
            throw new PaymentException("PayPal checkout is not configured.");
        }
    }
}
