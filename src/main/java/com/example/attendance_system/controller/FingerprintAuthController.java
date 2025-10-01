package com.example.attendance_system.controller;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fingerprint")
public class FingerprintAuthController {

    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public FingerprintAuthController(UserRepository userRepository, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginByFingerprint(@RequestParam Integer fingerprintId,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        Optional<User> optionalUser = userRepository.findByFingerprintId(fingerprintId);

        if (optionalUser.isEmpty()) {
            // lưu log Access Denied
            Log log = new Log();
            log.setUserId(null); // chưa xác định user
            log.setStatus("Login Failed (Fingerprint not recognized)");
            log.setTime(LocalDateTime.now());
            logRepository.save(log);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "fail", "message", "Fingerprint not recognized"));
        }

        User user = optionalUser.get();

        // tạo authority từ role
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        // tạo authentication token
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

        // set SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        // lưu context vào HttpSession
        request.getSession(true);
        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

        // lưu log Access Granted
        Log log = new Log();
        log.setUserId(user.getId());
        log.setStatus("Login Success (Fingerprint)");
        log.setTime(LocalDateTime.now());
        logRepository.save(log);

        Map<String, Object> responseBody = Map.of(
                "status", "success",
                "username", user.getUsername(),
                "role", user.getRole()
        );
        System.out.println(">>> Login API response = " + responseBody);

        return ResponseEntity.ok(responseBody);
    }
}
