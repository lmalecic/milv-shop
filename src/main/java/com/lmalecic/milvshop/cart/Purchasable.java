package com.lmalecic.milvshop.cart;

import java.math.BigDecimal;

public interface Purchasable {
    Long getPurchasableId();
    String getPurchasableName();
    PurchasableType getPurchasableType();
    BigDecimal getPurchasablePrice();
    String getPurchasableImageUrl();

    default PurchasableAttributes getPurchasableAttributes() {
        return PurchasableAttributes.empty();
    }
}
