package com.example.service.simple.jwt.authentication.application.usecase;

import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;

import java.util.Optional;

public interface LoginUserUseCase {

    Optional<String> createAuthenticationToken(UserLoginInfo userLoginInfo);
}
