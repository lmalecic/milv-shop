package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
