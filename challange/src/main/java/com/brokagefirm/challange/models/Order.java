package com.brokagefirm.challange.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
// builder can be used
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Asset asset;
    
    private OrderSide side;
    private OrderStatus status;
    private Integer quantity;
    private Integer price;
    private Date createdAt;
}
