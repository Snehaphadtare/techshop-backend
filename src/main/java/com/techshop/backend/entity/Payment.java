package com.techshop.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One payment belongs to one order
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    // CARD, UPI, COD
    @Column(nullable = false)
    private String paymentMethod;

    // SUCCESS, PENDING, FAILED
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Double amount;

    // Masked card like "****1234" or UPI id
    private String paymentReference;

    // Delivery address
    @Column(nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() {
        this.paidAt = LocalDateTime.now();
    }
}