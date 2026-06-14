package com.techshop.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Reads app.jwt.secret from application.properties
    @Value("${app.jwt.secret}")
    private String secret;

    // Reads app.jwt.expiration (86400000 ms = 24 hours)
    @Value("${app.jwt.expiration}")
    private long expiration;

    // Convert the secret string into a cryptographic signing key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Called on login → creates a JWT token containing the user's email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)                          // who this token is for
                .setIssuedAt(new Date())                    // when it was created
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // when it expires
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign it so nobody can fake it
                .compact();
    }

    // Extract the email from a token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if token is valid (not expired, not tampered)
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // expired, malformed, or wrong signature
        }
    }
}