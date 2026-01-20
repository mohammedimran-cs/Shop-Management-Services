package com.imran.shop.shop_management.service;

import com.imran.shop.shop_management.DTO.CategoryRequest;
import com.imran.shop.shop_management.DTO.CategoryResponse;
import com.imran.shop.shop_management.entity.Category;
import com.imran.shop.shop_management.exception.UserNotFoundException;
import com.imran.shop.shop_management.repository.CategoryRepository;
import com.imran.shop.shop_management.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private UserRepo userRepo;
    private Long getLoggedInUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }

    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {
        Long userId = getLoggedInUserId();
        if(!categoryRepo.findByNameContainingIgnoreCaseAndUserId(categoryRequest.name(),userId).isEmpty()){
            throw new UserNotFoundException("Category name already exist");
        }
        Category category = new Category();
        category.setName(categoryRequest.name());
        category.setUserId(userId);
        categoryRepo.save(category);
        return new CategoryResponse(
                category.getId(), category.getName());
    }

    public List<CategoryResponse> findAllCategories() {
        Long userId = getLoggedInUserId();
        return categoryRepo.findByUserId(userId)
                .stream().map(
                category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                )).toList();
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Category not found"));

        category.setName(request.name());

        Category updated = categoryRepo.save(category);

        return new CategoryResponse(updated.getId(), updated.getName());
    }

    public void deleteCategory(Long id) {

        if (!categoryRepo.existsById(id)) {
            throw new UserNotFoundException("Category not found");
        }

        categoryRepo.deleteById(id);
    }
}
