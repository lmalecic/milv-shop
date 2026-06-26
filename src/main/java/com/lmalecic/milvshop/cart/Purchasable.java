package com.lmalecic.milvshop.cart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
