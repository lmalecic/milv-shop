package com.lmalecic.milvshop.cart.resolver;

import com.lmalecic.milvshop.cart.Purchasable;
import com.lmalecic.milvshop.cart.PurchasableType;

public interface PurchasableResolver {
    PurchasableType getSupportedType();
    Purchasable resolve(Long id);
}
