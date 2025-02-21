package com.brokagefirm.challange.models.dtos;

import com.brokagefirm.challange.models.Asset;

@lombok.Data
public class StockItem {
    private Asset asset;
    private Integer quantity;
    private Integer availableQuantity;
}
