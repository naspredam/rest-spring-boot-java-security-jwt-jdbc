package com.example.service.simple.jwt.authentication.application.usecase;

import com.example.service.simple.jwt.authentication.domain.User;

public interface CreateNewUserUseCase {

    User createUser(User user);
}
