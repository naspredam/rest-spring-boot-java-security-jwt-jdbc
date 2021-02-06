package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateUserBodyDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String cpf;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String phone;

}
