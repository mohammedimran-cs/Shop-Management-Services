package com.imran.shop.shop_management.service;

import com.imran.shop.shop_management.DTO.ProductRequest;
import com.imran.shop.shop_management.DTO.ProductResponse;
import com.imran.shop.shop_management.entity.Category;
import com.imran.shop.shop_management.entity.Product;
import com.imran.shop.shop_management.exception.UserNotFoundException;
import com.imran.shop.shop_management.repository.CategoryRepository;
import com.imran.shop.shop_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public ProductResponse addProduct(ProductRequest req) {

        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new UserNotFoundException("Category not found"));

        if(productRepo.findByName(req.name()).isPresent()){
            throw new UserNotFoundException("This product name is already exist");
        }

        Product p = new Product();
        p.setName(req.name());
        p.setWholeSalePrice(req.wholeSalePrice());
        p.setPrice(req.price());
        p.setStock(req.stock());
        p.setBarcode(req.barcode());
        p.setCategory(category);

        Product saved = productRepo.save(p);

        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getWholeSalePrice(),
                saved.getPrice(),
                saved.getStock(),
                saved.getBarcode(),
                saved.getCategory().getId(),
                saved.getCategory().getName()
        );
    }

    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getWholeSalePrice(),
                        p.getPrice(),
                        p.getStock(),
                        p.getBarcode(),
                        p.getCategory().getId(),
                        p.getCategory().getName()
                ))
                .toList();
    }

    public ProductResponse updateProduct(Long id, ProductRequest req) {

        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Product not found"));

        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new UserNotFoundException("Category not found"));

        existing.setName(req.name());
        existing.setPrice(req.price());
        existing.setWholeSalePrice(req.wholeSalePrice());
        existing.setStock(req.stock());
        existing.setBarcode(req.barcode());
        existing.setCategory(category);

        Product saved = productRepo.save(existing);

        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getWholeSalePrice(),
                saved.getPrice(),
                saved.getStock(),
                saved.getBarcode(),
                saved.getCategory().getId(),
                saved.getCategory().getName()
        );
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public List<ProductResponse> searchByName(String name) {
        return productRepo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getWholeSalePrice(),
                        p.getPrice(),
                        p.getStock(),
                        p.getBarcode(),
                        p.getCategory().getId(),
                        p.getCategory().getName()
                ))
                .toList();
    }

    public List<ProductResponse> getByCategory(String category) {
        return productRepo.findByCategory_Name(category)
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getWholeSalePrice(),
                        p.getPrice(),
                        p.getStock(),
                        p.getBarcode(),
                        p.getCategory().getId(),
                        p.getCategory().getName()
                ))
                .toList();
    }
}

