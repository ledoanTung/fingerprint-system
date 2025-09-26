package com.example.attendance_system.service;

import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Esp32WebSocketClient extends WebSocketClient {

    private static Esp32WebSocketClient instance;
    private final FingerprintService fingerprintService;
    private final UserRepository userRepository;

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
                        fingerprintService.setLastFingerprintId(fingerprintId);
                        // TODO: gán fingerprintId cho user mới nếu cần
                    } else {
                        System.out.println("Enroll failed");
                    }

                    // --- Scan để login ---
                } else if ("scan".equalsIgnoreCase(action)) {
                    if (node.has("fingerprintId")) {
                        Integer fingerprintId = node.get("fingerprintId").asInt();

                        Optional<User> optionalUser = userRepository.findByFingerprintId(fingerprintId);

                        Map<String, Object> response = new HashMap<>();

                        if (optionalUser.isPresent()) {
                            User user = optionalUser.get();
                            System.out.println("Login success for user: " + user.getUsername());

                            response.put("status", "success");
                            response.put("username", user.getUsername());
                            response.put("role", user.getRole());
//                            fingerprintService.login(user);

                        } else {
                            System.out.println("Login failed: fingerprint not recognized");
                            response.put("status", "fail");
                            response.put("message", "Fingerprint not recognized");
                        }

                        // Gửi kết quả về FE qua STOMP WebSocket
                        fingerprintService.sendScanResultToFE(optionalUser.orElse(null));

                    } else {
                        System.out.println("Scan failed: no fingerprintId");
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
