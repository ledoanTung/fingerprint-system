package com.example.attendance_system.controller;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {
    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public DashboardController(UserRepository userRepository, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model,
//                            @RequestParam(name = "tab", defaultValue = "logs") String tab) {
//        if ("users".equals(tab)) {
//            model.addAttribute("users", userRepository.findAll());
//        } else if ("logs".equals(tab)) {
//            model.addAttribute("logs", logRepository.findAll());
//        }
//
//        model.addAttribute("tab", tab);
//        return "dashboard";
//    }

}
