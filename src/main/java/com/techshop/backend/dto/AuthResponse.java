package com.techshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;      // the JWT
    private String username;
    private String email;
    private String role;
}