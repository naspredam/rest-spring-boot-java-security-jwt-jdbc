package com.example.service.simple.jwt.authentication.controller.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginRequestBodyDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
