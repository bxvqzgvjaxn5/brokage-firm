package com.brokagefirm.challange.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByType(CustomerType type);
    Optional<Customer> findByEmail(String email);
}
