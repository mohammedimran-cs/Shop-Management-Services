package com.imran.shop.shop_management.repository;

import com.imran.shop.shop_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
     Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Object> findByVerificationToken(String token);

    Optional<Object> findByResetToken(String token);
}
