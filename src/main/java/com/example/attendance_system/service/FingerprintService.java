package com.example.attendance_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FingerprintService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private String lastFingerprintId = "";

    // Gửi lệnh "enroll" xuống ESP32
    public String enroll() {
        Esp32WebSocketClient.sendMessage("enroll");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lastFingerprintId.isEmpty() ? "Chưa có ID" : lastFingerprintId;
    }

    public void requestScan() {
        Esp32WebSocketClient.sendMessage("scan");
    }

    // Hàm này được WebSocket client gọi khi nhận phản hồi từ ESP32
    public void setLastFingerprintId(String id) {
        this.lastFingerprintId = id;
    }

public void sendScannedIdToFE(Integer fingerprintId) {
    Map<String, Object> payload = new HashMap<>();
    if (fingerprintId != null) {
        payload.put("status", "scanned");
        payload.put("fingerprintId", fingerprintId);
    } else {
        payload.put("status", "fail");
        payload.put("message", "Fingerprint not recognized");
    }
    messagingTemplate.convertAndSend("/topic/fingerprint", payload);
}
}
