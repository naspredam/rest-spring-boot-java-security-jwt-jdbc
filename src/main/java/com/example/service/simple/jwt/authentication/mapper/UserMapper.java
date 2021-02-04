package com.example.service.simple.jwt.authentication.mapper;

import com.example.service.simple.jwt.authentication.controller.dto.UserProfileDto;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileDto userDetails(UserData userData) {
        return UserProfileDto.builder()
                .username(userData.getUsername())
                .cpf(userData.getCpf())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .phone(userData.getPhone())
                .build();
    }

}
