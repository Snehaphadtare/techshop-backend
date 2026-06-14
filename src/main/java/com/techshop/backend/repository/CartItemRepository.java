package com.techshop.backend.repository;

import com.techshop.backend.entity.CartItem;
import com.techshop.backend.entity.User;
import com.techshop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Get all cart items belonging to a user
    List<CartItem> findByUser(User user);

    // Find a specific product already in a user's cart (to update qty instead of adding duplicate)
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    // Delete all cart items for a user (called after placing an order)
    void deleteByUser(User user);
}