package com.techshop.backend.repository;

import com.techshop.backend.entity.Order;
import com.techshop.backend.entity.OrderItem;
import com.techshop.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders for a user, newest first
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}