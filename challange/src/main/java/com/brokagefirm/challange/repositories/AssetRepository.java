package com.brokagefirm.challange.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brokagefirm.challange.models.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByName(String name);
}
