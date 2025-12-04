package com.example.ftp.service.middleware;

import org.springframework.stereotype.Component;

@Component
public class MaintenanceHandler extends AuthMiddleware {
    private boolean isMaintenanceMode = false;

    @Override
    public boolean check(String username, String userIp) {
        if (isMaintenanceMode) {
            if ("admin".equals(username))
                return checkNext(username, userIp);
            logger.warn("Login denied: Maintenance mode. User: " + username);
            return false;
        }
        return checkNext(username, userIp);
    }
}