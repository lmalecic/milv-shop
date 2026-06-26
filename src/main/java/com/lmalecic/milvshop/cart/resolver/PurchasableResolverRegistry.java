package com.lmalecic.milvshop.cart.resolver;

import com.lmalecic.milvshop.cart.PurchasableType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchasableResolverRegistry {

    private final List<PurchasableResolver> resolvers;

    public PurchasableResolver get(PurchasableType type) {
        for (PurchasableResolver resolver : this.resolvers) {
            if (resolver.getSupportedType() == type) {
                return resolver;
            }
        }

        throw new IllegalArgumentException("No Purchasable resolver found for type " + type);
    }
}
