package com.imran.shop.shop_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean enabled = false;   // account not active yet
    private String verificationToken;
    private LocalDateTime verificationTokenExpiry;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
}
