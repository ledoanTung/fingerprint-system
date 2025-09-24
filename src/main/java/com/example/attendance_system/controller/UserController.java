package com.example.attendance_system.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user-list")
    public String userList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/dashboard";

        session.setAttribute("page", "users");
        model.addAttribute("user", user);
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    // Tạo user mới
    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Kiểm tra fingerprintId có được truyền lên không
        if (user.getFingerprintId() == null) {
            return ResponseEntity.badRequest().body("Fingerprint ID is required");
        }
        // TODO: Mã hoá password trước khi lưu (nếu có bảo mật)
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id).map(u -> {
            u.setUsername(user.getUsername());
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            u.setFingerprintId(user.getFingerprintId());
            userRepository.save(u);
            return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }


}
