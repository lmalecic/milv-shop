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
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String PAYPAL_CREATE_REQUEST_ID_PREFIX = "milv-shop-paypal-create-order-";

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
                .paypalCreateRequestId(this.paypalCreateRequestId(paymentType))
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

    private String paypalCreateRequestId(PaymentType paymentType) {
        return paymentType == PaymentType.PAYPAL
                ? PAYPAL_CREATE_REQUEST_ID_PREFIX + UUID.randomUUID()
                : null;
    }

    @Transactional
    public void cancelPendingPayPalOrdersForUser(User user) {
        List<Order> pendingPayPalOrders = this.orderRepository.findAllByUserIdAndPaymentTypeAndStatus(
                user.getId(),
                PaymentType.PAYPAL,
                OrderStatus.PENDING
        );

        pendingPayPalOrders.forEach(order -> order.setStatus(OrderStatus.CANCELLED));
        this.orderRepository.saveAll(pendingPayPalOrders);
    }

    @Transactional
    public Order attachPayPalOrderId(Long orderId, String paypalOrderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with Id " + orderId + " not found."));
        order.setPaypalOrderId(paypalOrderId);
        return this.orderRepository.save(order);
    }

    @Transactional
    public Order completePayPalOrder(Long orderId, Long userId, String paypalOrderId) {
        Order order = this.findPendingPayPalOrderForUser(orderId, userId, paypalOrderId);
        order.setStatus(OrderStatus.COMPLETED);
        return this.orderRepository.save(order);
    }

    @Transactional
    public Order cancelPayPalOrder(Long orderId, Long userId, String paypalOrderId) {
        Order order = this.findPendingPayPalOrderForUser(orderId, userId, paypalOrderId);
        order.setStatus(OrderStatus.CANCELLED);
        return this.orderRepository.save(order);
    }

    public Order findPendingPayPalOrderForUser(Long orderId, Long userId, String paypalOrderId) {
        Order order = this.findPayPalOrderForUser(orderId, userId, paypalOrderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResourceNotFoundException("Pending PayPal order " + paypalOrderId + " not found.");
        }

        return order;
    }

    private Order findPayPalOrderForUser(Long orderId, Long userId, String paypalOrderId) {
        Order order = this.orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with Id " + orderId + " not found."));

        if (order.getPaymentType() != PaymentType.PAYPAL || !paypalOrderId.equals(order.getPaypalOrderId())) {
            throw new ResourceNotFoundException("PayPal order " + paypalOrderId + " not found.");
        }

        return order;
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

    public List<OrderDto> findAllOrdersByCriteria(OrderSearchCriteria criteria) {
        if (!criteria.hasActiveFilters()) {
            return this.findAllOrders();
        }
        return this.orderRepository.findAll(OrderSpecification.includeDeleted(false)
                                .and(OrderSpecification.userIdEquals(criteria.userQuery())
                                        .or(OrderSpecification.usernameLike(criteria.userQuery())))
                                .and(OrderSpecification.orderDateBetween(criteria.beforeDate(), criteria.afterDate())),
                        OrderSpecification.orderByOrderDateDesc())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<OrderDto> findAllUserOrdersByCriteria(User user, OrderSearchCriteria criteria) {
        if (!criteria.hasActiveFilters()) {
            return this.findAllUserOrders(user);
        }
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
                .paypalOrderId(order.getPaypalOrderId())
                .items(order.getItems().stream()
                        .map(this.orderItemService::toDto)
                        .toList())
                .build();
    }
}
