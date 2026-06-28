package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.cart.Purchasable;
import com.lmalecic.milvshop.cart.resolver.PurchasableResolver;
import com.lmalecic.milvshop.cart.resolver.PurchasableResolverRegistry;
import com.lmalecic.milvshop.dto.OrderItemDto;
import com.lmalecic.milvshop.entity.*;
import com.lmalecic.milvshop.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final PurchasableResolverRegistry purchasableResolverRegistry;

    public List<OrderItemDto> findAllByOrderId(Long orderId) {
        return this.orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<OrderItemDto> findById(Long id) {
        return this.orderItemRepository.findById(id)
                .map(this::toDto);
    }

    public OrderItemDto toDto(OrderItem orderItem) {
        PurchasableResolver resolver = this.purchasableResolverRegistry.get(orderItem.getItemType());
        Purchasable purchasable = resolver.resolve(orderItem.getItemId());
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .item(purchasable)
                .quantity(orderItem.getQuantity())
                .build();
    }
}
