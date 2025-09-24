package com.example.attendance_system.service;

import org.springframework.stereotype.Service;

@Service
public class FingerprintService {

    private String lastFingerprintId = "";

    // Gửi lệnh "enroll" xuống ESP32
    public String enroll() {
        // Gửi message đến ESP32 WebSocket
        Esp32WebSocketClient.sendMessage("enroll");

        // Chờ ESP32 phản hồi (ở đây demo chờ cứng 3 giây)
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lastFingerprintId.isEmpty() ? "Chưa có ID" : lastFingerprintId;
    }

    public void requestScan() {
        Esp32WebSocketClient.sendMessage("SCAN");
    }

    // Hàm này được WebSocket client gọi khi nhận phản hồi từ ESP32
    public void setLastFingerprintId(String id) {
        this.lastFingerprintId = id;
    }
}
