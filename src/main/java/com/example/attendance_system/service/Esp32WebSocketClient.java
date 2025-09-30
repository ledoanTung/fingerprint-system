package com.example.attendance_system.service;

import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Esp32WebSocketClient extends WebSocketClient {

    private static Esp32WebSocketClient instance;
    private final FingerprintService fingerprintService;
    private final UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Esp32WebSocketClient(URI serverUri, FingerprintService fingerprintService, UserRepository userRepository) {
        super(serverUri);
        this.fingerprintService = fingerprintService;
        this.userRepository = userRepository;
        instance = this;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to ESP32 WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(message);

            if (node.has("action")) {
                String action = node.get("action").asText();

                // --- Enroll vân tay ---
                if ("enroll".equalsIgnoreCase(action)) {
                    if (node.has("fingerprintId")) {
                        String fingerprintId = node.get("fingerprintId").asText();
                        System.out.println("Enrolled fingerprint: " + fingerprintId);

                        // Lưu fingerprintId tạm thời (nếu bạn muốn gán cho user mới)
                        fingerprintService.setLastFingerprintId(fingerprintId);
                    } else {
                        System.out.println("Enroll failed");
                    }

                    // --- Scan để login ---
                } else if ("scan".equalsIgnoreCase(action)) {
                    if (node.has("fingerprintId")) {
                        Integer fingerprintId = node.get("fingerprintId").asInt();
                        System.out.println("Scanned fingerprintId: " + fingerprintId);

                        // 🚨 KHÔNG gọi login ở đây
                        // Gửi fingerprintId về FE qua STOMP topic
                        fingerprintService.sendScannedIdToFE(fingerprintId);

                    } else {
                        System.out.println("Scan failed (no fingerprintId)");
                        fingerprintService.sendScannedIdToFE(null);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    // Hàm gửi lệnh từ BE xuống ESP32
    public static void sendMessage(String msg) {
        if (instance != null && instance.isOpen()) {
            instance.send(msg);
            System.out.println("Sent to ESP32: " + msg);
        } else {
            System.out.println("WebSocket not connected!");
        }
    }
}
