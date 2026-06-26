package com.lmalecic.milvshop.cart;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PurchasableAttributes {
    private List<BadgeAttribute> badges;

    public static PurchasableAttributes empty() {
        return PurchasableAttributes.builder().build();
    }
}
