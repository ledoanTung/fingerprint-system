package com.example.attendance_system.dto;

public class VerifyResponse {
    private boolean allow;
    private String message;
    private Long userId;

    public VerifyResponse(boolean allow, String message, Long userId) {
        this.allow = allow;
        this.message = message;
        this.userId = userId;
    }

    public boolean isAllow() {
        return allow;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }
}
