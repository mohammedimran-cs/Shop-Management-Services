package com.imran.shop.shop_management.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        String name,

        @NotNull(message = "Wholesale rate is required")
        @Positive(message = "Wholesale rate must be greater than 0")
        Double wholeSalePrice,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        Double price,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @NotBlank(message = "Barcode is required")
        String barcode,

        @NotNull(message = "Category is required")
        Long categoryId
) {
}

