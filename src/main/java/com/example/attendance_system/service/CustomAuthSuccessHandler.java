package com.example.attendance_system.service;

import com.example.attendance_system.model.Log;
import com.example.attendance_system.model.User;
import com.example.attendance_system.repository.LogRepository;
import com.example.attendance_system.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long userId = user.getId();

        Log log = new Log();
        log.setUserId(userId);
        log.setStatus("Access Granted");
        log.setTime(LocalDateTime.now());
        System.out.println("++++");
        logRepository.save(log);

        response.sendRedirect("/dashboard");
    }
}

