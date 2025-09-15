package com.example.attendance_system.controller;

import com.example.attendance_system.dto.VerifyRequest;
import com.example.attendance_system.dto.VerifyResponse;
import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VerifyController {

    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public VerifyController(UserRepository userRepository, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verifyFingerprint(@RequestBody VerifyRequest request) {
        Integer fid = request.getFingerprintId();
        boolean allow = false;
        String msg;
        Long userId = null;

        if (fid == null) {
            msg = "Missing fingerprintId";
        } else {
            Optional<User> userOpt = userRepository.findByFingerprintId(fid);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                allow = true;
                userId = user.getId();
                msg = "Access granted for " + user.getUsername();
            } else {
                msg = "Access denied: fingerprint not mapped";
            }
        }

        // Lưu log
        Log log = new Log();
        log.setUserId(userId);
        log.setStatus(allow ? "Access Granted" : "Access Denied");
        log.setTime(LocalDateTime.now());
        logRepository.save(log);

        VerifyResponse resp = new VerifyResponse(allow, msg, userId);
        return ResponseEntity.ok(resp);
    }
}
