package com.imran.shop.shop_management.DTO;

public record OrderItemRequest(
        Long productId,
        Integer qty
) {}

