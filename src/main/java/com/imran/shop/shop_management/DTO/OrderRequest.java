package com.imran.shop.shop_management.DTO;

import java.util.List;

public record OrderRequest(
        List<OrderItemRequest> items,
        Double discount
) {}

