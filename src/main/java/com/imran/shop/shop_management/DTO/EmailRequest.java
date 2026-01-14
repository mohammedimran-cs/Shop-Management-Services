package com.imran.shop.shop_management.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;


public record EmailRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email) {
}
