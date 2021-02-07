package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LoginRequestBodyDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
