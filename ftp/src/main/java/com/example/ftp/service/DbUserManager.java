package com.example.ftp.service;

import com.example.ftp.model.FtpUser;
import com.example.ftp.repository.UserRepository;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication; // <--- ДОДАНО ЦЕЙ ІМПОРТ
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DbUserManager implements UserManager {

    @Autowired
    private UserRepository userRepository;

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
        // Обмеження швидкості (download, upload)
        authorities.add(new TransferRatePermission(dbUser.getMaxDownloadRate(), dbUser.getMaxUploadRate()));

        // Максимальна кількість одночасних підключень (10 загалом, 5 з одного IP)
        authorities.add(new ConcurrentLoginPermission(10, 5));

        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
            FtpUser user = userRepository.findByUsername(auth.getUsername());

            // Проста перевірка пароля
            // Увага: Якщо user == null, треба викинути помилку, щоб не було
            // NullPointerException
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