package com.example.attendance_system.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
public class UserViewController {

    @GetMapping("/user-create")
    public String createUserPage() {
        return "user-create";
    }

    @GetMapping("/user/edit/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "user-edit";
    }
}
