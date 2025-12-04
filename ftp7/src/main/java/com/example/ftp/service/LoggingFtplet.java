package com.example.ftp.service;

import com.example.ftp.model.ConnectionLog;
import com.example.ftp.repository.ConnectionLogRepository;
import com.example.ftp.service.template.ImageProcessor;
import com.example.ftp.service.template.TextDocProcessor;
import org.apache.ftpserver.ftplet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class LoggingFtplet extends DefaultFtplet {

    @Autowired
    private ConnectionLogRepository logRepository;

    // --- Template Method Components (Lab 7) ---
    @Autowired
    private TextDocProcessor textProcessor;
    @Autowired
    private ImageProcessor imageProcessor;

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
        String filename = request.getArgument();

        logRepository.save(new ConnectionLog(user, session.getClientAddress().toString(), "UPLOAD: " + filename));

        // 2. ЗАПУСК ШАБЛОННОГО МЕТОДУ (Lab 7)
        try {
            String homeDir = session.getUser().getHomeDirectory();
            File uploadedFile = new File(homeDir, filename);

            if (filename.toLowerCase().endsWith(".txt") || filename.toLowerCase().endsWith(".log")) {
                textProcessor.processFile(uploadedFile, user);
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png")) {
                imageProcessor.processFile(uploadedFile, user);
            } else {
                System.out.println("--- [TEMPLATE] No specific processor for " + filename + " ---");
            }
        } catch (Exception e) {
            System.err.println("Error processing file template: " + e.getMessage());
        }

        return FtpletResult.DEFAULT;
    }

    @Override
    public FtpletResult onDeleteEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String user = session.getUser().getName();
        String filename = request.getArgument();

        logRepository.save(new ConnectionLog(user, session.getClientAddress().toString(), "DELETE: " + filename));

        return FtpletResult.DEFAULT;
    }
}