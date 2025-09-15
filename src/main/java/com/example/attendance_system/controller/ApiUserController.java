package com.example.attendance_system.controller;

import com.example.attendance_system.dto.FingerprintRequest;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiUserController {
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ApiUserController(UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @PutMapping("/users/{id}/fingerprint")
    public ResponseEntity<?> registerFingerprint(
            @PathVariable Long id,
            @RequestBody FingerprintRequest request
    ) {
        Integer fingerprintId = request.getFingerprintId();
        return userRepository.findById(id)
                .map(user -> {
                    user.setFingerprintId(fingerprintId);
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of(
                            "status", "success",
                            "userId", user.getId(),
                            "fingerprintId", user.getFingerprintId()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("status", "error", "message", "User not found")
                ));
    }
}