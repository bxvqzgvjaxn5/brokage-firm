package com.brokagefirm.challange.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brokagefirm.challange.aspects.RoleCheck;
import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.models.Order;
import com.brokagefirm.challange.services.AssetService;
import com.brokagefirm.challange.services.OrderService;

@RestController
public class AssetController {

    private final AssetService assetService;
    private final OrderService orderService;
    
    public AssetController(AssetService assetService, OrderService orderService) {
        this.assetService = assetService;
        this.orderService = orderService;
    }

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return assetService.getAssets();
    }

    @PostMapping("/assets")
    @RoleCheck(CustomerType.ADMIN)
    public Asset createAsset(@RequestBody Asset asset) {
        return assetService.createAsset(asset);
    }

    @DeleteMapping("/assets/{id}")
    @RoleCheck(CustomerType.ADMIN)
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }

    @PostMapping("/public-offer")
    @RoleCheck(CustomerType.ADMIN)
    public void publicOffer(@RequestBody Order order) {
        orderService.createPublicOffer(order.getAsset().getId(), order.getQuantity(), order.getPrice());
    }
}
