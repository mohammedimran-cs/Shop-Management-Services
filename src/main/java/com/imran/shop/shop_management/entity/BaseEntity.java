package com.imran.shop.shop_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;
}

