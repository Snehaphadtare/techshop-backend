package com.techshop.backend.controller;

import com.techshop.backend.entity.Product;
import com.techshop.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired private ProductService productService;

    // GET /api/products         → all products
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET /api/products/sale    → only sale items
    @GetMapping("/sale")
    public ResponseEntity<List<Product>> getSale() {
        return ResponseEntity.ok(productService.getSaleProducts());
    }

    // GET /api/products/1       → single product detail
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // GET /api/products/search?keyword=iphone
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // GET /api/products/category/Laptops
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> byCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getByCategory(category));
    }
}