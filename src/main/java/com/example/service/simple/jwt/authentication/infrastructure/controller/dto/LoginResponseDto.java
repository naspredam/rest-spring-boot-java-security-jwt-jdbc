package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LoginResponseDto extends RepresentationModel<LoginResponseDto> {

    private String token;

}
