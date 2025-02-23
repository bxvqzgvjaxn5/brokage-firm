package com.brokagefirm.challange.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokagefirm.challange.models.Order;
import com.brokagefirm.challange.models.OrderSide;
import com.brokagefirm.challange.models.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByCustomerIdOrderByCreatedAtAsc(Long customerId);
    Optional<List<Order>> findByStatusAndSideOrderByCreatedAtAsc(OrderStatus status, OrderSide side);
}
