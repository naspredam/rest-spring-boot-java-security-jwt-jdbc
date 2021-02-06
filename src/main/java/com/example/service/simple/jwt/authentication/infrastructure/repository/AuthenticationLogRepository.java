package com.example.service.simple.jwt.authentication.infrastructure.repository;

import com.example.service.simple.jwt.authentication.infrastructure.repository.data.AuthenticationLogData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLogData, Long> {

    boolean existsByIdAndInvalidatedFalse(Long logId);

}