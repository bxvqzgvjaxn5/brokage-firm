package com.brokagefirm.challange.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.brokagefirm.challange.models.Asset;
import com.brokagefirm.challange.repositories.AssetRepository;

@Service
public class AssetService {
    private AssetRepository assetRepository;
    private String mainAssetName;

    public AssetService(AssetRepository assetRepository, @Value("${brokagefirm.mainasset.name}") String mainAssetName) {
        this.assetRepository = assetRepository;
        this.mainAssetName = mainAssetName;
    }

    public Asset getAsset(Long id) {
        Optional<Asset> asset = this.assetRepository.findById(id);
        if (!asset.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Asset not found with id: " + id);
        }
        return asset.get();
    }

    public Asset createAsset(Asset asset) {
        Optional<Asset> assetO = this.assetRepository.findByName(asset.getName());
        if (assetO.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Asset with name: " + asset.getName() + " already exists");
        }
        return this.assetRepository.save(asset);
    }

    public Asset getMainAsset() {
        Optional<Asset> asset = this.assetRepository.findByName(mainAssetName);
        if (!asset.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Main Asset not found");
        }
        return asset.get();
    }

    public List<Asset> getAssets() {
        return this.assetRepository.findAll();
    }

    public void deleteAsset(Long id) {
        Optional<Asset> asset = this.assetRepository.findById(id);
        if (!asset.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Asset not found with id: " + id);
        }
        this.assetRepository.delete(asset.get());
    }
}
