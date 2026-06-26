package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.entity.OrderStatus;
import com.lmalecic.milvshop.entity.PaymentType;
import lombok.Builder;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDto (
    @With Long id,
    UserDto user,
    LocalDateTime orderDate,
    PaymentType paymentType,
    OrderStatus status,
    List<OrderItemDto> items
) {}
