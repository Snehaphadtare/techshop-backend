package com.techshop.backend.controller;

import com.techshop.backend.entity.CartItem;
import com.techshop.backend.entity.User;
import com.techshop.backend.service.CartService;
import com.techshop.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    private User getCurrentUser(Authentication auth) {
        String email = auth.getName(); // now always returns email
        return userService.getUserByEmail(email);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(getCurrentUser(auth)));
    }

    @PostMapping
    public ResponseEntity<CartItem> addToCart(@RequestBody Map<String, Integer> body,
                                              Authentication auth) {
        Long productId = Long.valueOf(body.get("productId"));
        int quantity = body.getOrDefault("quantity", 1);
        return ResponseEntity.ok(
                cartService.addToCart(getCurrentUser(auth), productId, quantity)
        );
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId,
                                               Authentication auth) {
        cartService.removeFromCart(getCurrentUser(auth), cartItemId);
        return ResponseEntity.noContent().build();
    }
}