package com.example.service.simple.jwt.authentication.infrastructure.repository;

import com.example.service.simple.jwt.authentication.infrastructure.repository.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Long> {

    Optional<UserData> findByUsername(String username);

}