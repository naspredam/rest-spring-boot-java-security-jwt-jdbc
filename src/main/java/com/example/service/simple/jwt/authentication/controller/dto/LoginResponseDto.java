package com.example.service.simple.jwt.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class LoginResponseDto {

    private final String token;

}
