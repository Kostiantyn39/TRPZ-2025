package com.example.ftp.service.strategy;

import com.example.ftp.model.FtpUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class SpeedLimitContext {
    @Autowired
    private Map<String, SpeedLimitStrategy> strategies;

    public TransferRatePermission getPermission(FtpUser user) {
        SpeedLimitStrategy strategy;
        if (user.getUsername().startsWith("admin")) {
            strategy = strategies.get("vipStrategy");
        } else if (user.getUsername().endsWith("_night")) {
            strategy = strategies.get("nightStrategy");
        } else {
            strategy = strategies.get("defaultStrategy");
        }
        return strategy.calculateLimit(user);
    }
}