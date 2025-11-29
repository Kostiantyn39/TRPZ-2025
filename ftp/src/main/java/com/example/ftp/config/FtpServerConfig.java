package com.example.ftp.config;

import com.example.ftp.service.DbUserManager;
import com.example.ftp.service.LoggingFtplet;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;
import org.apache.ftpserver.ftplet.Ftplet;

@Configuration
public class FtpServerConfig {

    @Autowired
    private DbUserManager dbUserManager;

    @Autowired
    private LoggingFtplet loggingFtplet;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public FtpServer ftpServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ListenerFactory factory = new ListenerFactory();
        factory.setPort(2121); // Використовуємо порт 2121
        serverFactory.addListener("default", factory.createListener());

        serverFactory.setUserManager(dbUserManager);

        // Підключаємо логування
        Map<String, Ftplet> ftplets = new HashMap<>();
        ftplets.put("logging", loggingFtplet);
        serverFactory.setFtplets(ftplets);

        return serverFactory.createServer();
    }
}