package com.techshop.backend.repository;

import com.techshop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // SELECT * FROM products WHERE on_sale = true
    List<Product> findByOnSaleTrue();

    // SELECT * FROM products WHERE category = ?
    List<Product> findByCategory(String category);

    // SELECT * FROM products WHERE brand = ?
    List<Product> findByBrand(String brand);

    // SELECT * FROM products WHERE name LIKE %keyword% OR description LIKE %keyword%
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description
    );
}