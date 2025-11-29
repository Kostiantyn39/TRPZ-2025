package com.example.ftp.service.strategy;

import com.example.ftp.model.FtpUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.springframework.stereotype.Component;

@Component("defaultStrategy")
public class DefaultLimitStrategy implements SpeedLimitStrategy {
    @Override
    public TransferRatePermission calculateLimit(FtpUser user) {
        return new TransferRatePermission(user.getMaxDownloadRate(), user.getMaxUploadRate());
    }
}