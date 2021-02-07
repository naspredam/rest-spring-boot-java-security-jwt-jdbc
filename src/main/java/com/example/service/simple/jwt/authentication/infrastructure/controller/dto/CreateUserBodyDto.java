package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateUserBodyDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String nationalId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String phone;

}
