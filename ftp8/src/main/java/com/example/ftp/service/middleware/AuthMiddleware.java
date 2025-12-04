package com.example.ftp.service.middleware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AuthMiddleware {
    private AuthMiddleware next;
    protected static final Logger logger = LoggerFactory.getLogger(AuthMiddleware.class);

    public AuthMiddleware linkWith(AuthMiddleware next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(String username, String userIp);

    protected boolean checkNext(String username, String userIp) {
        if (next == null) return true;
        return next.check(username, userIp);
    }
}