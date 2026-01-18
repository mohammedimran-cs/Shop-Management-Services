package com.imran.shop.shop_management.DTO;

public record ProductResponse(
        Long id,
        String name,
        Double wholeSalePrice,
        Double price,
        Integer stock,
        String barcode,
        Long categoryId,
        String categoryName
) {}

