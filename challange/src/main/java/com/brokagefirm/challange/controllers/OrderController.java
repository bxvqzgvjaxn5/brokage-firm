package com.brokagefirm.challange.controllers;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.brokagefirm.challange.AuthHelper;
import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.models.Order;
import com.brokagefirm.challange.models.OrderSide;
import com.brokagefirm.challange.models.OrderStatus;
import com.brokagefirm.challange.services.CustomerService;
import com.brokagefirm.challange.services.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping("/order-sell")
    public Order createOrderSell(@RequestBody Order order) {
        Customer sessionCustomer = customerService.getCustomer(AuthHelper.getAuthUsername());
        if (!sessionCustomer.getType().equals(CustomerType.ADMIN) && !sessionCustomer.getId().equals(order.getCustomer().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        order.setStatus(OrderStatus.PENDING);
        order.setSide(OrderSide.SELL);
        order.setId(null);

        Order createdOrder = orderService.createOrder(order);
        orderService.applyOrder(createdOrder);
        return createdOrder;
    }

    @PostMapping("/order-buy")
    public Order createOrderBuy(@RequestBody Order order) {
        Customer sessionCustomer = customerService.getCustomer(AuthHelper.getAuthUsername());
        if (!sessionCustomer.getType().equals(CustomerType.ADMIN) && !sessionCustomer.getId().equals(order.getCustomer().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        order.setStatus(OrderStatus.PENDING);
        order.setSide(OrderSide.BUY);
        order.setId(null);

        orderService.applyOrder(order);
        return order;
    }

    @DeleteMapping("/order-cancel")
    public void deleteOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);

        Customer sessionCustomer = customerService.getCustomer(AuthHelper.getAuthUsername());
        if (!sessionCustomer.getType().equals(CustomerType.ADMIN) && !sessionCustomer.getId().equals(order.getCustomer().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        orderService.cancelOrder(id);
    }

}
