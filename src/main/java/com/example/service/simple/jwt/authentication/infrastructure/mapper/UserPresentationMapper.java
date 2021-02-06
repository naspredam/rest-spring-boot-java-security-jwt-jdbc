package com.example.service.simple.jwt.authentication.infrastructure.mapper;

import com.example.service.simple.jwt.authentication.domain.User;
import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;
import com.example.service.simple.jwt.authentication.domain.UserPersonalInfo;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.CreateUserBodyDto;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.UserProfileDto;
import org.springframework.stereotype.Component;

@Component
public class UserPresentationMapper {

    public User domainUser(CreateUserBodyDto bodyDto) {
        return User.builder()
                .loginInfo(UserLoginInfo.of(bodyDto.getUsername(), bodyDto.getPassword()))
                .personalInfo(UserPersonalInfo.builder()
                        .cpf(bodyDto.getCpf())
                        .firstName(bodyDto.getFirstName())
                        .lastName(bodyDto.getLastName())
                        .phone(bodyDto.getPhone())
                        .build())
                .build();
    }

    public UserProfileDto presentationUserDetails(User user) {
        UserPersonalInfo personalInfo = user.getPersonalInfo();
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getLoginInfo().getUsername())
                .cpf(personalInfo.getCpf())
                .firstName(personalInfo.getFirstName())
                .lastName(personalInfo.getLastName())
                .phone(personalInfo.getPhone())
                .build();
    }

}
