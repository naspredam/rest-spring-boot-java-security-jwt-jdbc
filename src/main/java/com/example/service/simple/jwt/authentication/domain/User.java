package com.example.service.simple.jwt.authentication.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class User {

    private final Long id;

    private final UserLoginInfo loginInfo;

    private final UserPersonalInfo personalInfo;
}
