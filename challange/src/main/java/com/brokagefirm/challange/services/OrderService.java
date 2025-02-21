package com.brokagefirm.challange.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.Order;
import com.brokagefirm.challange.models.OrderSide;
import com.brokagefirm.challange.models.OrderStatus;
import com.brokagefirm.challange.models.dtos.StockItem;
import com.brokagefirm.challange.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AssetService assetService;
    private final CustomerService customerService;

    private String mainAssetName;

    public OrderService(
            OrderRepository orderRepository,
            AssetService assetService,
            CustomerService customerService,
            @Value("${brokagefirm.mainasset.name}") String mainAssetName) {
        this.orderRepository = orderRepository;
        this.assetService = assetService;
        this.customerService = customerService;
        this.mainAssetName = mainAssetName;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        if (order.getAsset().getName().contentEquals(mainAssetName)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Main asset cannot be traded");
        }
        order.setId(null);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(new Date());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Optional<Order> orderO = orderRepository.findById(id);
        if (!orderO.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found with id: " + id);
        }

        Order order = orderO.get();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public void initSellOrderForBrokerageFirm(Long assetId, Integer quantity, Integer price) {
        Customer customer = customerService.getAssetVendorCustomer();
        Asset savedAsset = assetService.getAsset(assetId);

        Order order = new Order();
        order.setCustomer(customer);
        order.setAsset(savedAsset);
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setSide(OrderSide.SELL);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(new Date());
        orderRepository.save(order);
    }

    public void initBuyOrderForCustomer(Long customerId, Integer quantity) {
        Customer customer = customerService.getCustomer(customerId);
        Asset savedAsset = assetService.getMainAsset();

        Order order = new Order();
        order.setCustomer(customer);
        order.setAsset(savedAsset);
        order.setQuantity(quantity);
        order.setPrice(Integer.valueOf(1));
        order.setSide(OrderSide.BUY);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(new Date());
        orderRepository.save(order);
    }

    public StockItem getStockItem(Long customerId, Long assetId) {
        // check if customer exists
        customerService.getCustomer(customerId);
        Asset asset = assetService.getAsset(assetId);

        StockItem stock = new StockItem();
        stock.setAsset(asset);
        stock.setQuantity(0);
        stock.setAvailableQuantity(0);

        Optional<List<Order>> ordersO = orderRepository.findByCustomerIdOrderByCreatedAtAsc(customerId);
        if (!ordersO.isPresent()) {
            return stock;
        }

        List<Order> orders = ordersO.get();
        for (Order order : orders) {
            // TODO decompose conditions
            if (asset.getName().contentEquals(mainAssetName)) {
                Integer totalMainAssetQuantity = order.getQuantity() * order.getPrice();
                if (order.getSide() == OrderSide.SELL && order.getStatus() == OrderStatus.MATCHED) {
                    stock.setQuantity(stock.getQuantity() + totalMainAssetQuantity);
                    stock.setAvailableQuantity(stock.getAvailableQuantity() + totalMainAssetQuantity);
                } else if (order.getSide() == OrderSide.BUY && order.getStatus() == OrderStatus.MATCHED) {
                    stock.setQuantity(stock.getQuantity() - totalMainAssetQuantity);
                    stock.setAvailableQuantity(stock.getAvailableQuantity() - totalMainAssetQuantity);
                } else if (order.getSide() == OrderSide.BUY && order.getStatus() == OrderStatus.PENDING) {
                    stock.setAvailableQuantity(stock.getAvailableQuantity() - totalMainAssetQuantity);
                }
            } else if (order.getAsset().getId() == assetId) {
                if (order.getSide() == OrderSide.BUY && order.getStatus() == OrderStatus.MATCHED) {
                    stock.setQuantity(stock.getQuantity() + order.getQuantity());
                    stock.setAvailableQuantity(stock.getAvailableQuantity() + order.getQuantity());
                } else if (order.getSide() == OrderSide.SELL && order.getStatus() == OrderStatus.MATCHED) {
                    stock.setQuantity(stock.getQuantity() - order.getQuantity());
                    stock.setAvailableQuantity(stock.getAvailableQuantity() - order.getQuantity());
                } else if (order.getSide() == OrderSide.SELL && order.getStatus() == OrderStatus.PENDING) {
                    stock.setAvailableQuantity(stock.getAvailableQuantity() - order.getQuantity());
                }
            }
        }

        return stock;
    }
}
