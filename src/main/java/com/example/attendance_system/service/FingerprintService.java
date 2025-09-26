package com.example.attendance_system.service;

import com.example.attendance_system.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FingerprintService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private HttpSession session;

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
        Esp32WebSocketClient.sendMessage("scan");
    }

    // Hàm này được WebSocket client gọi khi nhận phản hồi từ ESP32
    public void setLastFingerprintId(String id) {
        this.lastFingerprintId = id;
    }

    public void sendScanResultToFE(User user) {
        Map<String, Object> data = new HashMap<>();
        if (user != null) {
            data.put("status", "success");
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
        } else {
            data.put("status", "fail");
            data.put("message", "Fingerprint not recognized");
        }

        // Gửi tới FE, FE subscribe /topic/fingerprint
        messagingTemplate.convertAndSend("/topic/fingerprint", data);
    }


    public String login(User user) {
        // Lưu user vào session
        session.setAttribute("user", user);

        // Tùy theo role bạn có thể lưu thêm thông tin
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            session.setAttribute("page", "dashboard");
            return "redirect:/dashboard";
        } else {
            session.setAttribute("page", "logs");
            return "redirect:/dashboard";
        }
    }

}
