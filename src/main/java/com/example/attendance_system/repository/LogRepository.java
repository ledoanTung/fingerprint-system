package com.example.attendance_system.repository;


import com.example.attendance_system.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByUserId(Long userId);
    
}
