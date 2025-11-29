package com.example.ftp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connection_logs")
public class ConnectionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String clientIp;
    private String action; // LOGIN, UPLOAD, DOWNLOAD
    private LocalDateTime timestamp;

    public ConnectionLog() {
    }

    public ConnectionLog(String username, String clientIp, String action) {
        this.username = username;
        this.clientIp = clientIp;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}