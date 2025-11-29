package com.example.ftp.service;

import com.example.ftp.model.ConnectionLog;
import com.example.ftp.repository.ConnectionLogRepository;
import org.apache.ftpserver.ftplet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFtplet extends DefaultFtplet {

    @Autowired
    private ConnectionLogRepository logRepository;

    @Override
    public FtpletResult onLogin(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String user = session.getUser().getName();
        String ip = session.getClientAddress().toString();
        logRepository.save(new ConnectionLog(user, ip, "LOGIN_SUCCESS"));
        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String user = session.getUser().getName();
        String ip = session.getClientAddress().toString();
        logRepository.save(new ConnectionLog(user, ip, "UPLOAD: " + request.getArgument()));
        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String user = session.getUser().getName();
        String ip = session.getClientAddress().toString();
        logRepository.save(new ConnectionLog(user, ip, "DOWNLOAD: " + request.getArgument()));
        return FtpletResult.DEFAULT;
    }
}