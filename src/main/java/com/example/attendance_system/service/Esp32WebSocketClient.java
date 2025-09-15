package com.example.attendance_system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Esp32WebSocketClient extends WebSocketClient {

    private static Esp32WebSocketClient instance;
    private static FingerprintService fingerprintService;

    public Esp32WebSocketClient(URI serverUri, FingerprintService service) {
        super(serverUri);
        fingerprintService = service;
        instance = this;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to ESP32 WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received from ESP32: " + message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(message);
            if (node.has("fingerprintId")) {
                String id = node.get("fingerprintId").asText();
                fingerprintService.setLastFingerprintId(id);
                System.out.println("Fingerprint ID parsed: " + id);
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