package com.lmalecic.milvshop.service.paypal;

public record PayPalCheckoutResult(String orderId, String approvalUrl) {
}
