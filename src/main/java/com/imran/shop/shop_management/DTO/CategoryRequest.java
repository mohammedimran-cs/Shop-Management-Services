package com.imran.shop.shop_management.DTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Category name is required")
        String name
) {
}
