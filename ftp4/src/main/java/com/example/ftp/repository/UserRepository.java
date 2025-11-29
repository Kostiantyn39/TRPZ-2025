package com.example.ftp.repository;

import com.example.ftp.model.FtpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<FtpUser, Long> {
    FtpUser findByUsername(String username);
}