package com.techshop.backend.controller;

import com.techshop.backend.dto.PaymentRequest;
import com.techshop.backend.entity.Order;
import com.techshop.backend.entity.OrderItem;
import com.techshop.backend.entity.Payment;
import com.techshop.backend.entity.User;
import com.techshop.backend.service.OrderService;
import com.techshop.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.getUserByEmail(auth.getName());
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            @RequestBody PaymentRequest paymentRequest,
            Authentication auth) {
        return ResponseEntity.ok(
                orderService.placeOrder(getCurrentUser(auth), paymentRequest)
        );
    }

    @GetMapping
    public ResponseEntity<List<Order>> getHistory(Authentication auth) {
        return ResponseEntity.ok(orderService.getOrderHistory(getCurrentUser(auth)));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderItems(orderId));
    }

    @GetMapping("/{orderId}/payment")
    public ResponseEntity<Payment> getPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getPaymentByOrderId(orderId));
    }
}