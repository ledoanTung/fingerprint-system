package com.example.attendance_system.controller;

import com.example.attendance_system.model.User;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fingerprint")
public class FingerprintAuthController {

    private final UserRepository userRepository;

    public FingerprintAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginByFingerprint(@RequestParam Integer fingerprintId,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        Optional<User> optionalUser = userRepository.findByFingerprintId(fingerprintId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "fail", "message", "Fingerprint not recognized"));
        }

        User user = optionalUser.get();

        // tạo authority từ role (đảm bảo role lưu là "ADMIN" hoặc "USER" etc)
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        // tạo authentication token (username làm principal)
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

        // tạo và set SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        // lưu context vào HttpSession (rất quan trọng)
        request.getSession(true); // tạo session nếu chưa có
        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

        Map<String, Object> responseBody = Map.of(
                "status", "success",
                "username", user.getUsername(),
                "role", user.getRole()
        );
        System.out.println(">>> Login API response = " + responseBody);
        return ResponseEntity.ok(responseBody);
    }
}