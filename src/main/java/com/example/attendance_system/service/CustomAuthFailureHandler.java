package com.example.attendance_system.service;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");

        Long userId = null;
        if (username != null && !username.isBlank()) {
            userId = userRepository.findByUsername(username)
                    .map(User::getId) // Nếu có user thì lấy id
                    .orElse(null);    // Nếu không có thì để null
        }

        // Ghi log thất bại
        Log log = new Log();
        log.setUserId(userId);
        log.setStatus("Access Denied");
        log.setTime(LocalDateTime.now());
        logRepository.save(log);

        response.sendRedirect("/login");
    }
}
