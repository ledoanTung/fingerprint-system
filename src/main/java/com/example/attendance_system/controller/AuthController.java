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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

//    @PostMapping("/login")
//    public String doLogin(@RequestParam String username,
//                          @RequestParam String password,
//                          HttpSession session,
//                          Model model) {
//
//        User user = userRepository.findByUsername(username);
//
//        if (user != null && user.getPassword().equals(password)) {
//            // Lưu user vào session
//            session.setAttribute("user", user);
//
//            // Redirect theo role
//            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
//                session.setAttribute("page", "dashboard");
//                return "redirect:/dashboard";
//            } else {
//                session.setAttribute("page", "logs");
//                return "redirect:/dashboard";
//            }
//        }
//
//        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
//        return "login";
//    }

    // Logout
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/login";
//    }
}