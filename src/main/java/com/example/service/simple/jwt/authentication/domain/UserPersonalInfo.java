package com.example.service.simple.jwt.authentication.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPersonalInfo {

    private final String cpf;

    private final String firstName;

    private final String lastName;

    private final String phone;
}
