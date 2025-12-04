package com.example.ftp.service.middleware;

import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class BlacklistHandler extends AuthMiddleware {
    private final Set<String> blacklistedIps = Set.of("192.168.1.666");

    @Override
    public boolean check(String username, String userIp) {
        if (blacklistedIps.contains(userIp)) {
            logger.error("Login blocked: IP " + userIp + " is blacklisted.");
            return false;
        }
        return checkNext(username, userIp);
    }
}