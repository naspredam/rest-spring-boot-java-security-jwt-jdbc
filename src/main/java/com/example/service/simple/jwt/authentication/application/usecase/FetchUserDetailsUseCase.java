package com.example.service.simple.jwt.authentication.application.usecase;

import com.example.service.simple.jwt.authentication.domain.User;

import java.util.Optional;

public interface FetchUserDetailsUseCase {

    Optional<User> fetchLoggedUserData();
}
