package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.Order;
import com.lmalecic.milvshop.entity.OrderStatus;
import com.lmalecic.milvshop.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByUserIdOrderByOrderDateDesc(Long userId);
    List<Order> findAllByUserIdAndPaymentTypeAndStatus(Long userId, PaymentType paymentType, OrderStatus status);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
