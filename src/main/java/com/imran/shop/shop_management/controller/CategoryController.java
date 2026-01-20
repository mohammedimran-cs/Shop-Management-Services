package com.imran.shop.shop_management.controller;

import com.imran.shop.shop_management.DTO.CategoryRequest;
import com.imran.shop.shop_management.DTO.CategoryResponse;
import com.imran.shop.shop_management.entity.Category;
import com.imran.shop.shop_management.exception.UserNotFoundException;
import com.imran.shop.shop_management.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse addCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.saveCategory(categoryRequest);
    }

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.findAllCategories();
    }

    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable Long id,
            @RequestBody CategoryRequest request) {

        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}

