package com.example.ftp.service.middleware;

import org.springframework.stereotype.Component;

@Component
public class AuditLogHandler extends AuthMiddleware {
    @Override
    public boolean check(String username, String userIp) {
        logger.info("[AUDIT] Auth attempt: User={} IP={}", username, userIp);
        return checkNext(username, userIp);
    }
}