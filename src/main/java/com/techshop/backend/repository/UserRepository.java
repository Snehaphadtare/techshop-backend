package com.techshop.backend.repository;

import com.techshop.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring sees "findByEmail" and auto-generates:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // SELECT COUNT(*) FROM users WHERE email = ?  → returns true/false
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}