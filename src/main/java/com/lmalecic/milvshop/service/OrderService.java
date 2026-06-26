package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.entity.*;
import com.lmalecic.milvshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order create(User user, Cart cart, PaymentType paymentType) {
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .paymentType(paymentType)
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> items = cart.getItems().values().stream()
                .map(cartItem -> OrderItem.builder()
                        .itemType(cartItem.getItemType())
                        .itemId(cartItem.getItemId())
                        .quantity(cartItem.getQuantity())
                        .order(order)
                        .build())
                .toList();

        order.setItems(items);
        return this.orderRepository.save(order);
    }
}
