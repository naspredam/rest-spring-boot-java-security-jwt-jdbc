package com.example.service.simple.jwt.authentication.controller;

import com.example.service.simple.jwt.authentication.controller.dto.LoginRequestBodyDto;
import com.example.service.simple.jwt.authentication.controller.dto.LoginResponseDto;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import com.example.service.simple.jwt.authentication.factory.TokenFactory;
import com.example.service.simple.jwt.authentication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    private final TokenFactory tokenFactory;

    public UserController(UserService userService, TokenFactory tokenFactory) {
        this.userService = userService;
        this.tokenFactory = tokenFactory;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestBodyDto loginRequestBodyDto) {
        log.info("start logging...");
        String username = loginRequestBodyDto.getUsername();
        String password = loginRequestBodyDto.getPassword();
        return userService.fetchByUserNameAndPwd(username, password)
                .map(UserData::publicAttributes)
                .map(tokenFactory::createNewToken)
                .map(LoginResponseDto::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("message", "user and password not found...")
                        .build());
    }

    @GetMapping("/principals")
    public Principal retrievePrincipal(Principal principal) {
        log.info("retrievePrincipal - Start...");
        return principal;
    }
}
