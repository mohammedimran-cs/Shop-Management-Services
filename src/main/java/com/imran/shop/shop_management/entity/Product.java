package com.imran.shop.shop_management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
    private double wholeSalePrice;
    private double price;
    private int stock;
    private String barcode;   // Important for POS
}
