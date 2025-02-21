package com.brokagefirm.challange.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokagefirm.challange.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByCustomerIdOrderByCreatedAtAsc(Long customerId);
}
