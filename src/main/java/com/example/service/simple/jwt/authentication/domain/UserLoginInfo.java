package com.example.service.simple.jwt.authentication.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserLoginInfo {

    String username;

    String password;

    public static UserLoginInfo fromUsernameOnly(String username) {
        return UserLoginInfo.of(username, null);
    }
}
