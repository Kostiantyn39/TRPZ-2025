package com.example.ftp.controller;

import com.example.ftp.model.ConnectionLog;
import com.example.ftp.model.FtpUser;
import com.example.ftp.repository.ConnectionLogRepository;
import com.example.ftp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

// SERVER PART: REST API Controller
// Цей клас забезпечує інтерфейс для зовнішніх клієнтських програм
@RestController
@RequestMapping("/api")
public class ApiAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionLogRepository logRepository;

    // 1. Отримати список користувачів (JSON)
    @GetMapping("/users")
    public List<FtpUser> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Додати користувача (JSON input)
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody FtpUser user) {
        // Створення папки
        File home = new File(user.getHomeDirectory());
        if (!home.exists()) {
            home.mkdirs();
        }

        // Важлива логіка: якщо ID прийшов 0 або null, JPA створить нового.
        // Якщо ID прийшов заповнений - оновить існуючого.
        userRepository.save(user);
        return ResponseEntity.ok("User saved successfully with ID: " + user.getId());
    }

    // 3. Отримати статистику (JSON)
    @GetMapping("/logs")
    public List<ConnectionLog> getLogs() {
        return logRepository.findAll();
    }
}