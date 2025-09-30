package com.example.attendance_system.controller;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LogController {

    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/logs")
    public String logs(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Lấy username từ authentication
        String username = authentication.getName();

        // Tìm user từ DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("user", user);
        model.addAttribute("page", "logs");

        List<Log> logs;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            logs = logRepository.findAll();
        } else {
            logs = logRepository.findByUserId(user.getId());
        }

        model.addAttribute("logs", logs);
        return "logs";
    }
}