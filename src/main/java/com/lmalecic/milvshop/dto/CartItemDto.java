package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.cart.PurchasableType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemDto(
        @NotNull PurchasableType itemType,
        @NotNull Long itemId,
        @Positive @Max(CartItemDto.MAX_QUANTITY) @NotNull Integer quantity
) {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 99;
}
