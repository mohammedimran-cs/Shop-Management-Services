package com.imran.shop.shop_management.controller;

import com.imran.shop.shop_management.DTO.ProductRequest;
import com.imran.shop.shop_management.DTO.ProductResponse;
import com.imran.shop.shop_management.entity.Product;
import com.imran.shop.shop_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse addProduct(@Valid @RequestBody ProductRequest req) {
        return productService.addProduct(req);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest req) {

        return productService.updateProduct(id, req);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{name}")
    public List<ProductResponse> search(@PathVariable String name) {
        return productService.searchByName(name);
    }

    @GetMapping("/category/{name}")
    public List<ProductResponse> byCategory(@PathVariable String name) {
        return productService.getByCategory(name);
    }
}

