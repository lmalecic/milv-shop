package com.lmalecic.milvshop.cart;

import lombok.Getter;

@Getter
public enum PurchasableType {
    TANK("Tank");

    private final String displayName;

    PurchasableType(String displayName) {
        this.displayName = displayName;
    }
}
