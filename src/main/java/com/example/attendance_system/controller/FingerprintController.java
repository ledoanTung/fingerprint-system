package com.example.attendance_system.controller;

import com.example.attendance_system.model.User;
import com.example.attendance_system.service.Esp32WebSocketClient;
import com.example.attendance_system.service.FingerprintService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fingerprint")
public class FingerprintController {
    private final FingerprintService fingerprintService;

    public FingerprintController(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @GetMapping("/enroll")
    public String enrollFingerprint() {
        return fingerprintService.enroll();
    }

//    @PostMapping("/scan")
//    public ResponseEntity<?> requestScan() {
//        // gửi lệnh SCAN xuống ESP32
//        fingerprintService.requestScan();
//        return ResponseEntity.ok(Map.of(
//                "status", "waiting",
//                "message", "Đã gửi lệnh scan xuống ESP32, vui lòng đặt ngón tay."
//        ));
//    }


    @MessageMapping("/scan")
    public void scanFingerprint() {
        fingerprintService.requestScan();
        System.out.println("Sent scan request to ESP32");
    }

}
