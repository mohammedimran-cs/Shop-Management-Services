package com.imran.shop.shop_management.DTO;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        double totalAmount,
        LocalDateTime billDate,
        String createdBy
) {}

