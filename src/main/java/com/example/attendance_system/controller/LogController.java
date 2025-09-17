package com.example.attendance_system.controller;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LogController {

    @Autowired
    private LogRepository logRepository;

//    @GetMapping("/logs")
//    public String showLogs(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null) return "redirect:/login";
//
//        List<Log> logs;
//        if ("ADMIN".equals(user.getRole())) {
//            logs = logRepository.findAll(); // admin xem tất cả
//        } else {
//            logs = logRepository.findByUserId(user.getId()); // user chỉ xem của mình
//        }
//
//        model.addAttribute("logs", logs);
//        model.addAttribute("user", user);
//        return "logs";
//    }

    @GetMapping("/logs")
    public String logs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        session.setAttribute("page", "logs");
        model.addAttribute("user", user);

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