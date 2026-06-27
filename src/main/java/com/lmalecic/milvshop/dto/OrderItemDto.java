package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.cart.Purchasable;
import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@Builder
public record OrderItemDto(
    @With Long id,
    Purchasable item,
    Integer quantity
) {
    public BigDecimal getTotalPrice() {
        return this.item.getPurchasablePrice().multiply(BigDecimal.valueOf(this.quantity));
    }
}
