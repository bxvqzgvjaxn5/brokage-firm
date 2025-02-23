package com.brokagefirm.challange.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.brokagefirm.challange.AuthHelper;
import com.brokagefirm.challange.aspects.RoleCheck;
import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.models.dtos.StockItemDTO;
import com.brokagefirm.challange.services.AssetService;
import com.brokagefirm.challange.services.CustomerService;
import com.brokagefirm.challange.services.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @RoleCheck(CustomerType.ADMIN)
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@RequestParam Long id) {
        Customer customer = customerService.getCustomer(id);
        AuthHelper.validateCustomer(customer);
        return customerService.getCustomer(id);
    }

    @PostMapping("/customers")
    @RoleCheck(CustomerType.ADMIN)
    public void createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        orderService.depositMoney(savedCustomer.getId(), 100);
    }

    @PostMapping("/customers/{id}/deposit")
    @RoleCheck(CustomerType.ADMIN)
    public void depositMoney(@RequestParam Long id, @RequestParam Integer amount) {
        orderService.depositMoney(id, amount);
    }

    @GetMapping("/customers/{id}/stocks")
    public List<StockItemDTO> getStocks(@PathVariable Long id) {
        AuthHelper.validateCustomer(customerService.getCustomer(id));

        List<Asset> assets = assetService.getAssets();
        List<StockItemDTO> stocks = new ArrayList<>();
        for (Asset asset : assets) {
            StockItemDTO stock = orderService.getStockItem(id, asset.getId());
            if (stock.getQuantity() > 0) {
                stocks.add(stock);
            }
        }
        return stocks;
    }

    @GetMapping("/customers/{id}/stocks/{assetId}")
    public StockItemDTO getStock(@PathVariable Long id, @PathVariable Long assetId) {
        AuthHelper.validateCustomer(customerService.getCustomer(id));
        return orderService.getStockItem(id, assetId);
    }
}
