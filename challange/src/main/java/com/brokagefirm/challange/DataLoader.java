package com.brokagefirm.challange;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.models.Order;
import com.brokagefirm.challange.models.OrderSide;
import com.brokagefirm.challange.models.OrderStatus;
import com.brokagefirm.challange.repositories.AssetRepository;
import com.brokagefirm.challange.repositories.CustomerRepository;
import com.brokagefirm.challange.repositories.OrderRepository;


@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;
    private final OrderRepository orderRepository;
    private final String mainAssetName;

    public DataLoader(
        CustomerRepository customerRepository,
        AssetRepository assetRepository,
        OrderRepository orderRepository,
        @Value("${brokagefirm.mainasset.name}") String mainAssetName
    ) {
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
        this.orderRepository = orderRepository;
        this.mainAssetName = mainAssetName;
    }

    @Override
    @Transactional
    public void run(String... args) {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        assetRepository.deleteAll();

        Customer customerAssetVendor = new Customer();
        customerAssetVendor.setEmail("admin@example.com");
        customerAssetVendor.setPassword("password");
        customerAssetVendor.setType(CustomerType.ASSET_VENDOR);
        customerRepository.save(customerAssetVendor);

        Customer customerInvestor1 = new Customer();
        customerInvestor1.setEmail("investor1@example.com");
        customerInvestor1.setPassword("password");
        customerInvestor1.setType(CustomerType.INVESTOR);
        customerRepository.save(customerInvestor1);

        Customer customerInvestor2 = new Customer();
        customerInvestor2.setEmail("investor0@example.com");
        customerInvestor2.setPassword("password");
        customerInvestor2.setType(CustomerType.INVESTOR);
        customerRepository.save(customerInvestor2);

        Asset mainAsset = new Asset();
        mainAsset.setName(mainAssetName);
        assetRepository.save(mainAsset);

        Asset asset1 = new Asset();
        asset1.setName("STOCKA");
        assetRepository.save(asset1);

        Asset asset2 = new Asset();
        asset2.setName("STOCKB");
        assetRepository.save(asset2);

        Order order1 = new Order();
        order1.setAsset(mainAsset);
        order1.setCustomer(customerInvestor1);
        order1.setQuantity(50);
        order1.setPrice(1);
        order1.setSide(OrderSide.SELL);
        order1.setStatus(OrderStatus.MATCHED);
        order1.setCreatedAt(new Date());
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setAsset(mainAsset);
        order2.setCustomer(customerInvestor2);
        order2.setQuantity(70);
        order2.setPrice(1);
        order2.setSide(OrderSide.SELL);
        order2.setStatus(OrderStatus.MATCHED);
        order2.setCreatedAt(new Date());
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setAsset(asset1);
        order3.setCustomer(customerAssetVendor);
        order3.setQuantity(20);
        order3.setPrice(2);
        order3.setSide(OrderSide.SELL);
        order3.setStatus(OrderStatus.PENDING);
        order3.setCreatedAt(new Date());
        orderRepository.save(order3);

        Order order4 = new Order();
        order4.setAsset(asset2);
        order4.setCustomer(customerAssetVendor);
        order4.setQuantity(10);
        order4.setPrice(3);
        order4.setSide(OrderSide.SELL);
        order4.setStatus(OrderStatus.PENDING);
        order4.setCreatedAt(new Date());
        orderRepository.save(order4);
    }
}
