package com.example.ftp.repository;

import com.example.ftp.model.ConnectionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionLogRepository extends JpaRepository<ConnectionLog, Long> {
}