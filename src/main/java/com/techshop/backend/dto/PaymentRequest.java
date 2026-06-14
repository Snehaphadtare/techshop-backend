package com.techshop.backend.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String paymentMethod;   // "CARD", "UPI", "COD"
    private String paymentReference; // "****1234" or UPI id or "COD"
    private String addressLine;
    private String city;
    private String pincode;
    private String phone;
}