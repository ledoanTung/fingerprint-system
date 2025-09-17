package com.example.attendance_system.controller;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        session.setAttribute("page", "dashboard");
        model.addAttribute("user", user);
        return "dashboard";
    }

}
