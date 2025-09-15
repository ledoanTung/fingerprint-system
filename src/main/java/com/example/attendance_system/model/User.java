package com.example.attendance_system.model;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String role;
    @Column(unique = true)
    private Integer fingerprintId;

    public User() {
    }

    public User(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String username, String password, String role, Integer fingerprintId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fingerprintId = fingerprintId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getFingerprintId() {
        return fingerprintId;
    }
    public void setFingerprintId(Integer fingerprintId) {
        this.fingerprintId = fingerprintId;
    }
}