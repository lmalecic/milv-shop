package com.lmalecic.milvshop.cart.resolver;

import com.lmalecic.milvshop.cart.Purchasable;
import com.lmalecic.milvshop.cart.PurchasableType;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.repository.TankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TankPurchasableResolver implements PurchasableResolver {

    private final TankRepository tankRepository;

    @Override
    public PurchasableType getSupportedType() {
        return PurchasableType.TANK;
    }

    @Override
    public Purchasable resolve(Long id) {
        return this.tankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + id + "not found!"));
    }
}
