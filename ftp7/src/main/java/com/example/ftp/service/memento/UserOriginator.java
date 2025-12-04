package com.example.ftp.service.memento;

import com.example.ftp.model.FtpUser;
import org.springframework.stereotype.Component;

@Component
public class UserOriginator {

    public FtpUserMemento save(FtpUser user) {
        return new FtpUserMemento(
                user.getHomeDirectory(),
                user.isCanWrite(),
                user.isCanRead(),
                user.getMaxUploadRate(),
                user.getMaxDownloadRate());
    }

    // Метод відновлення (Restore State)
    public void restore(FtpUser user, FtpUserMemento memento) {
        user.setHomeDirectory(memento.getHomeDirectory());
        user.setCanWrite(memento.isCanWrite());
        user.setCanRead(memento.isCanRead());
        user.setMaxUploadRate(memento.getMaxUploadRate());
        user.setMaxDownloadRate(memento.getMaxDownloadRate());
    }
}