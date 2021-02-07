package com.example.service.simple.jwt.authentication.util;

import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.CreateUserBodyDto;
import com.example.service.simple.jwt.authentication.infrastructure.repository.data.UserData;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

public class DataFaker {

    private static final Faker FAKER = new Faker();

    private static final FakeValuesService FAKE_VALUES_SERVICE = new FakeValuesService(Locale.CANADA, new RandomService());

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private static String fakeUsername() {
        return FAKER.name().username();
    }

    public static String fakePassword() {
        return FAKER.internet().password();
    }

    private static String fakeFirstName() {
        return FAKER.name().firstName();
    }

    private static String fakeLastName() {
        return FAKER.name().lastName();
    }

    private static String fakeNationalId() {
        return FAKE_VALUES_SERVICE.regexify("\\d{11}");
    }

    private static String fakePhone() {
        return FAKE_VALUES_SERVICE.regexify("\\(\\+[1-9]\\d{1,2}\\) [0-9]{1,3}[0-9\\-]{6,9}[0-9]{1}");
    }

    public static UserData fakeNewUserData(String pwd) {
        return UserData.builder()
                .username(fakeUsername())
                .password(PASSWORD_ENCODER.encode(pwd))
                .nationalId(fakeNationalId())
                .firstName(fakeFirstName())
                .lastName(fakeLastName())
                .phone(fakePhone())
                .build();
    }

    public static CreateUserBodyDto fakeCreateUser() {
        CreateUserBodyDto bodyDto = new CreateUserBodyDto();
        bodyDto.setUsername(fakeUsername());
        bodyDto.setPassword(fakePassword());
        bodyDto.setNationalId(fakeNationalId());
        bodyDto.setFirstName(fakeFirstName());
        bodyDto.setLastName(fakeLastName());
        bodyDto.setPhone(fakePhone());
        return bodyDto;
    }
}
