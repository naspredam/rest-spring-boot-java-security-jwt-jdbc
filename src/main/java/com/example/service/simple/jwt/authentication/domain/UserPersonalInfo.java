package com.example.service.simple.jwt.authentication.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPersonalInfo {

    private final String nationalId;

    private final String firstName;

    private final String lastName;

    private final String phone;
}
