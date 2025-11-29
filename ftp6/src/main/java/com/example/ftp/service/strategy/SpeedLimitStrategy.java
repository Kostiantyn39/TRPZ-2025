package com.example.ftp.service.strategy;

import com.example.ftp.model.FtpUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;

public interface SpeedLimitStrategy {
    TransferRatePermission calculateLimit(FtpUser user);
}