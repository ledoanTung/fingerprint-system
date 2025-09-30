package com.example.attendance_system.controller;

import com.example.attendance_system.service.FingerprintService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @MessageMapping("/scan")
    public void scanFingerprint() {
        fingerprintService.requestScan();
        System.out.println("Sent scan request to ESP32");
    }

}
