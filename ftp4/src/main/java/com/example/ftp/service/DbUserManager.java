package com.example.ftp.service;

import com.example.ftp.model.FtpUser;
import com.example.ftp.repository.UserRepository;
import com.example.ftp.service.middleware.*;
import com.example.ftp.service.strategy.SpeedLimitContext;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DbUserManager implements UserManager {

    @Autowired
    private UserRepository userRepository;

    // Lab 4 Integration
    @Autowired
    private SpeedLimitContext speedLimitContext;

    // Lab 5 Integration
    @Autowired
    private MaintenanceHandler maintenanceHandler;
    @Autowired
    private BlacklistHandler blacklistHandler;
    @Autowired
    private AuditLogHandler auditLogHandler;

    @Override
    public User getUserByName(String username) throws FtpException {
        FtpUser dbUser = userRepository.findByUsername(username);
        if (dbUser == null)
            return null;

        BaseUser user = new BaseUser();
        user.setName(dbUser.getUsername());
        user.setPassword(dbUser.getPassword());
        user.setHomeDirectory(dbUser.getHomeDirectory());
        user.setEnabled(true);
        user.setMaxIdleTime(dbUser.getMaxIdleTime());

        List<Authority> authorities = new ArrayList<>();
        if (dbUser.isCanWrite()) {
            authorities.add(new WritePermission());
        }

        // STRATEGY PATTERN (Lab 4) - Динамічний розрахунок швидкості
        authorities.add(speedLimitContext.getPermission(dbUser));

        authorities.add(new ConcurrentLoginPermission(0, 0));
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
            String username = auth.getUsername();
            // Емуляція IP, бо FtpServer не передає його в цей метод просто так
            String userIp = "127.0.0.1";

            // CHAIN OF RESPONSIBILITY (Lab 5) - Ланцюжок перевірок
            maintenanceHandler.linkWith(blacklistHandler).linkWith(auditLogHandler);

            if (!maintenanceHandler.check(username, userIp)) {
                throw new AuthenticationFailedException("Blocked by security middleware");
            }

            FtpUser user = userRepository.findByUsername(username);
            if (user != null && user.getPassword().equals(auth.getPassword())) {
                try {
                    return getUserByName(user.getUsername());
                } catch (FtpException e) {
                    throw new AuthenticationFailedException("Error loading user", e);
                }
            }
        }
        throw new AuthenticationFailedException("Authentication failed");
    }

    @Override
    public String getAdminName() throws FtpException {
        return "admin";
    }

    @Override
    public boolean isAdmin(String username) throws FtpException {
        return "admin".equals(username);
    }

    @Override
    public boolean doesExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public void save(User user) throws FtpException {
    }

    @Override
    public void delete(String username) throws FtpException {
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return new String[0];
    }
}