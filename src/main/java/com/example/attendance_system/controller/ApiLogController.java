package com.example.attendance_system.controller;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiLogController {

    @Autowired
    private LogRepository logRepository;

    @PostMapping("/logs")
    public Log saveLog(@RequestBody Log log) {
        log.setTime(LocalDateTime.now()); // auto set thời gian hiện tại
        return logRepository.save(log);
    }
    @GetMapping("/logs")
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    @GetMapping("/logs/user/{userId}")
    public List<Log> getLogsByUser(@PathVariable Long userId) {
        return logRepository.findByUserId(userId);
    }
}