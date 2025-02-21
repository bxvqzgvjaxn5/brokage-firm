package com.brokagefirm.challange.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.brokagefirm.challange.AnnotationAuth;
import com.brokagefirm.challange.AnnotationAuthCheck;
import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.models.dtos.StockItem;
import com.brokagefirm.challange.services.AssetService;
import com.brokagefirm.challange.services.CustomerService;
import com.brokagefirm.challange.services.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final AssetService assetService;

    public CustomerController(
        CustomerService customerService,
        OrderService orderService,
        AssetService assetService
    ) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.assetService = assetService;
    }

    @GetMapping("/customers")
    @AnnotationAuthCheck(CustomerType.ADMIN)
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customers/{id}")
    @AnnotationAuthCheck(CustomerType.ADMIN)
    public Customer getCustomer(@RequestParam Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping("/customers")
    @AnnotationAuthCheck(CustomerType.ADMIN)
    public void createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        orderService.depositMoney(savedCustomer.getId(), 100);
    }

    @PostMapping("/customers/{id}/deposit")
    @AnnotationAuthCheck(CustomerType.ADMIN)
    public void depositMoney(@RequestParam Long id, @RequestParam Integer amount) {
        orderService.depositMoney(id, amount);
    }

    @PostMapping("/customers/{id}/stocks")
    public List<StockItem> getStocks(@RequestParam Long id) {
        
        AnnotationAuth.validateCustomer(customerService.getCustomer(id));

        List<Asset> assets = assetService.getAssets();
        List<StockItem> stocks = new ArrayList<>();
        for (Asset asset : assets) {
            StockItem stock = orderService.getStockItem(id, asset.getId());
        }
        return stocks;
    }
}
