package com.example.attendance_system.controller;

import com.example.attendance_system.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        session.setAttribute("page", "dashboard");
        model.addAttribute("user", user);
        return "dashboard";
    }

}
