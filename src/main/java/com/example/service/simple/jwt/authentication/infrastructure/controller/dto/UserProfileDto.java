package com.example.service.simple.jwt.authentication.infrastructure.controller.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
public class UserProfileDto extends RepresentationModel<UserProfileDto> {

    private final Long id;

    private final String username;

    private final String nationalId;

    private final String firstName;

    private final String lastName;

    private final String phone;
}
