package com.example.attendance_system.config;

import com.example.attendance_system.service.Esp32WebSocketClient;
import com.example.attendance_system.service.FingerprintService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.net.URI;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public Esp32WebSocketClient esp32WebSocketClient(FingerprintService fingerprintService) throws Exception {
        String esp32Uri = "ws://192.168.137.51:81/"; // 👈 thay bằng IP ESP32 của bạn
        Esp32WebSocketClient client = new Esp32WebSocketClient(new URI(esp32Uri), fingerprintService);
        client.connect();
        return client;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // client subscribe prefix /topic
        config.enableSimpleBroker("/topic");
        // prefix khi client gửi lên server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint client FE hoặc ESP32 sẽ connect
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}
