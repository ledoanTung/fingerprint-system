package com.example.attendance_system.repository;


import com.example.attendance_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByFingerprintId(Integer fingerprintId);
}
