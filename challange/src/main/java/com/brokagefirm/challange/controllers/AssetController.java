package com.brokagefirm.challange.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.services.AssetService;

@RestController
public class AssetController {

    private AssetService assetService;
    
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return assetService.getAssets();
    }

    @PostMapping("/assets")
    public Asset createAsset(@RequestBody Asset asset) {
        return assetService.createAsset(asset);
    }

    @DeleteMapping("/assets/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}
