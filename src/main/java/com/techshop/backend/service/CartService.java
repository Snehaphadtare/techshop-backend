package com.techshop.backend.service;

import com.techshop.backend.entity.CartItem;
import com.techshop.backend.entity.Product;
import com.techshop.backend.entity.User;
import com.techshop.backend.repository.CartItemRepository;
import com.techshop.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;

    public List<CartItem> getCart(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // If product already in cart → increase quantity instead of adding duplicate
        Optional<CartItem> existing = cartItemRepository.findByUserAndProduct(user, product);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void removeFromCart(User user, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Security check: make sure this cart item belongs to this user
        if (!item.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Unauthorized");

        cartItemRepository.delete(item);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}