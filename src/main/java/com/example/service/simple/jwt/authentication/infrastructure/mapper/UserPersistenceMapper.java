package com.example.service.simple.jwt.authentication.infrastructure.mapper;

import com.example.service.simple.jwt.authentication.domain.User;
import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;
import com.example.service.simple.jwt.authentication.domain.UserPersonalInfo;
import com.example.service.simple.jwt.authentication.infrastructure.repository.data.UserData;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User domain(UserData userData) {
        return User.builder()
                .id(userData.getId())
                .loginInfo(UserLoginInfo.fromUsernameOnly(userData.getUsername()))
                .personalInfo(buildPersonalInfo(userData))
                .build();
    }

    public UserData newData(User user) {
        return UserData.builder()
                .username(user.getLoginInfo().getUsername())
                .password(user.getLoginInfo().getPassword())
                .firstName(user.getPersonalInfo().getFirstName())
                .lastName(user.getPersonalInfo().getLastName())
                .nationalId(user.getPersonalInfo().getNationalId())
                .phone(user.getPersonalInfo().getPhone())
                .build();
    }

    private UserPersonalInfo buildPersonalInfo(UserData userData) {
        return UserPersonalInfo.builder()
                .nationalId(userData.getNationalId())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .phone(userData.getPhone())
                .build();
    }
}
