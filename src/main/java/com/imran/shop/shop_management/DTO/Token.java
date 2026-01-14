package com.imran.shop.shop_management.DTO;

import jakarta.validation.constraints.NotBlank;

public record Token(@NotBlank(message = "Token is required") String token) {
}
