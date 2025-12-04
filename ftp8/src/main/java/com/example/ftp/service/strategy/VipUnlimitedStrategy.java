package com.example.ftp.service.strategy;

import com.example.ftp.model.FtpUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.springframework.stereotype.Component;

@Component("vipStrategy")
public class VipUnlimitedStrategy implements SpeedLimitStrategy {
    @Override
    public TransferRatePermission calculateLimit(FtpUser user) {
        return new TransferRatePermission(0, 0); // Безліміт
    }
}