package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor(staticName = "of")
public class LoginResponseDto extends RepresentationModel<LoginResponseDto> {

    private final String token;

}
