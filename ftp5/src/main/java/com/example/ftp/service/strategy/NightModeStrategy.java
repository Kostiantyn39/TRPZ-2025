package com.example.ftp.service.strategy;

import com.example.ftp.model.FtpUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component("nightStrategy")
public class NightModeStrategy implements SpeedLimitStrategy {
    @Override
    public TransferRatePermission calculateLimit(FtpUser user) {
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.MIDNIGHT) && now.isBefore(LocalTime.of(8, 0))) {
            return new TransferRatePermission(0, 0); // Вночі безліміт
        }
        return new TransferRatePermission(user.getMaxDownloadRate(), user.getMaxUploadRate());
    }
}