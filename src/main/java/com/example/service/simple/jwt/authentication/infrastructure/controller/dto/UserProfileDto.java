package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDto {

    private final Long id;

    private final String username;

    private final String cpf;

    private final String firstName;

    private final String lastName;

    private final String phone;
}