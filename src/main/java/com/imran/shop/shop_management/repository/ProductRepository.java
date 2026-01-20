package com.imran.shop.shop_management.repository;

import com.imran.shop.shop_management.DTO.ProductResponse;
import com.imran.shop.shop_management.entity.Product;
import com.imran.shop.shop_management.service.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);
    List<Product> findByCategory_Name(String categoryName);

    List<Product> findByNameContainingIgnoreCaseAndUserId(String name, Long userId);

    List<Product> findByUserId(Long userId);
}
