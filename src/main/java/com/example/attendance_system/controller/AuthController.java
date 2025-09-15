package com.example.attendance_system.controller;

import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Trang login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Xử lý login
    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);

            // Nếu là admin → vào dashboard
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/dashboard";
            } else {
                // Nếu là user thường thì vào logs
                return "redirect:/logs";
            }
        }
        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}