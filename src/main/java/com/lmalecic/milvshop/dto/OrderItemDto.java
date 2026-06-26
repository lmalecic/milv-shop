package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.cart.Purchasable;
import lombok.Builder;
import lombok.With;

@Builder
public record OrderItemDto(
    @With Long id,
    Purchasable item,
    Integer quantity
) { }
