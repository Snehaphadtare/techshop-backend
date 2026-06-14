package com.techshop.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT") // allows long descriptions
    private String description;

    private String brand;       // e.g. "Samsung", "Apple"
    private String category;    // e.g. "Laptops", "Phones"

    @Column(nullable = false)
    private Double price;       // regular price

    private Double salePrice;   // discounted price (null if not on sale)

    @Column(nullable = false)
    private Boolean onSale = false;  // default: not on sale

    @Column(nullable = false)
    private Integer stockQty = 0;

    private String imageUrl;    // URL to product image
}