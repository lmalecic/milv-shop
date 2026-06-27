package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.cart.Cart;
import com.lmalecic.milvshop.criteria.OrderSearchCriteria;
import com.lmalecic.milvshop.dto.OrderDto;
import com.lmalecic.milvshop.entity.*;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.repository.OrderRepository;
import com.lmalecic.milvshop.specification.OrderSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;

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

    public OrderDto setStatus(Long orderId, OrderStatus orderStatus) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with Id " + orderId + " not found."));
        order.setStatus(orderStatus);
        return this.toDto(this.orderRepository.save(order));
    }

    public List<OrderDto> findAllOrders() {
        return this.orderRepository.findAll(OrderSpecification.orderByOrderDateDesc())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<OrderDto> findAllUserOrders(User user) {
        return this.orderRepository.findAllByUserIdOrderByOrderDateDesc(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<OrderDto> findAllUserOrdersByCriteria(User user, OrderSearchCriteria criteria) {
        return this.orderRepository.findAll(OrderSpecification.includeDeleted(false)
                        .and(OrderSpecification.userIdEquals(user.getId()))
                        .and(OrderSpecification.orderDateBetween(criteria.beforeDate(), criteria.afterDate())),
                        OrderSpecification.orderByOrderDateDesc())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<OrderDto> findById(Long id) {
        return this.orderRepository.findById(id)
                .map(this::toDto);
    }

    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .user(this.userService.toDto(order.getUser()))
                .orderDate(order.getOrderDate())
                .paymentType(order.getPaymentType())
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(this.orderItemService::toDto)
                        .toList())
                .build();
    }
}
