package com.techshop.backend.service;

import com.techshop.backend.dto.AuthResponse;
import com.techshop.backend.dto.LoginRequest;
import com.techshop.backend.dto.RegisterRequest;
import com.techshop.backend.entity.User;
import com.techshop.backend.repository.UserRepository;
import com.techshop.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already in use");
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already taken");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid email or password");
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole());
    }

    public User getUserByEmail(String email) {
        // trim whitespace just in case
        return userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}