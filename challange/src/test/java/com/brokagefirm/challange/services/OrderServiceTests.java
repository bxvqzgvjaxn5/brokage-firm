package com.brokagefirm.challange.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.models.Order;
import com.brokagefirm.challange.models.OrderSide;
import com.brokagefirm.challange.models.OrderStatus;
import com.brokagefirm.challange.models.dtos.StockItem;
import com.brokagefirm.challange.repositories.OrderRepository;

public class OrderServiceTests {
    private OrderRepository mockOrderRepository;
    private AssetService mockAssetService;
    private CustomerService mockCustomerService;

    private OrderService orderService;
    private static final String MAIN_ASSET_NAME = "TRY";

    @BeforeEach
    public void setUp() {
        this.mockOrderRepository = mock(OrderRepository.class);
        this.mockAssetService = mock(AssetService.class);
        this.mockCustomerService = mock(CustomerService.class);
        this.orderService = new OrderService(this.mockOrderRepository, this.mockAssetService, this.mockCustomerService, MAIN_ASSET_NAME);
    }

    @Test
    public void testGetStockItem() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("investor@example.com");
        customer.setType(CustomerType.INVESTOR);

        when(this.mockCustomerService.getCustomer(anyLong()))
            .thenReturn(customer);

        Asset asset = new Asset();
        asset.setId(1L);
        asset.setName("Asset");
        when(this.mockAssetService.getAsset(anyLong()))
            .thenReturn(asset);

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(
            1L, customer, asset, OrderSide.BUY, OrderStatus.MATCHED, 10, 1, null
        ));
        // 10
        // 10

        orders.add(new Order(
            2L, customer, asset, OrderSide.BUY, OrderStatus.PENDING, 10, 1, null
        ));
        // 10 + 0
        // 10 + 0

        orders.add(new Order(
            3L, customer, asset, OrderSide.SELL, OrderStatus.MATCHED, 2, 1, null
        ));
        // 10 + 0 - 2
        // 10 + 0 - 2

        orders.add(new Order(
            4L, customer, asset, OrderSide.SELL, OrderStatus.PENDING, 2, 1, null
        ));
        // 10 + 0 - 2 - 0
        // 10 + 0 - 2 - 2

        orders.add(new Order(
            5L, customer, asset, OrderSide.BUY, OrderStatus.CANCELLED, 1000, 1, null
        ));
        // 10 + 0 - 2 - 0 + 0
        // 10 + 0 - 2 - 2 + 0

        when(this.mockOrderRepository.findByCustomerIdOrderByCreatedAtAsc(anyLong()))
            .thenReturn(Optional.of(orders));

        StockItem stockItem = this.orderService.getStockItem(1L, 1L);
        assertEquals(8, stockItem.getQuantity());
        assertEquals(6, stockItem.getAvailableQuantity());

    }


    @Test
    public void testGetStockItem_whenAssetIsMainAsset() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("investor@example.com");
        customer.setType(CustomerType.INVESTOR);

        when(this.mockCustomerService.getCustomer(anyLong()))
            .thenReturn(customer);

        Asset mainAsset = new Asset();
        mainAsset.setId(0L);
        mainAsset.setName("TRY");

        Asset asset = new Asset();
        asset.setId(1L);
        asset.setName("Asset");
        
        when(this.mockAssetService.getAsset(anyLong()))
            .thenReturn(mainAsset);

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(
            0L, customer, mainAsset, OrderSide.SELL, OrderStatus.MATCHED, 100, 1, null
        ));
        // 100
        // 100

        orders.add(new Order(
            1L, customer, asset, OrderSide.BUY, OrderStatus.MATCHED, 10, 2, null
        ));
        // 100 - 20
        // 100 - 20

        orders.add(new Order(
            2L, customer, asset, OrderSide.BUY, OrderStatus.PENDING, 10, 3, null
        ));
        // 100 - 20 + 0
        // 100 - 20 - 30

        orders.add(new Order(
            3L, customer, asset, OrderSide.SELL, OrderStatus.MATCHED, 2, 1, null
        ));
        // 100 - 20 + 0 + 2
        // 100 - 20 - 30 + 2

        orders.add(new Order(
            4L, customer, asset, OrderSide.SELL, OrderStatus.PENDING, 2, 1, null
        ));
        // 100 - 20 + 0 + 2 + 0
        // 100 - 20 - 30 + 2 + 0

        orders.add(new Order(
            5L, customer, asset, OrderSide.BUY, OrderStatus.CANCELLED, 1000, 1, null
        ));
        // 100 - 20 + 0 + 2 + 0 + 0
        // 100 - 20 - 30 + 2 + 0 + 0

        when(this.mockOrderRepository.findByCustomerIdOrderByCreatedAtAsc(anyLong()))
            .thenReturn(Optional.of(orders));

        StockItem stockItem = this.orderService.getStockItem(1L, 0L);
        assertEquals(82, stockItem.getQuantity());
        assertEquals(52, stockItem.getAvailableQuantity());

    }

}