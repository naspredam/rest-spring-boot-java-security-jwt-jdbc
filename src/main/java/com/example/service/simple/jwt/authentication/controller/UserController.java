package com.example.service.simple.jwt.authentication.controller;

import com.example.service.simple.jwt.authentication.config.authentication.UserDetailsDto;
import com.example.service.simple.jwt.authentication.controller.dto.LoginRequestBodyDto;
import com.example.service.simple.jwt.authentication.controller.dto.LoginResponseDto;
import com.example.service.simple.jwt.authentication.controller.dto.UserProfileDto;
import com.example.service.simple.jwt.authentication.mapper.UserMapper;
import com.example.service.simple.jwt.authentication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestBodyDto bodyDto) {
        log.info("start logging...");
        String username = bodyDto.getUsername();
        String password = bodyDto.getPassword();
        return userService.createTokenByUsernameAndPwd(username, password)
                .map(userMapper::loginResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("message", "user and password not found...")
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> retrievePrincipal(Authentication authentication) {
        log.info("retrievePrincipal - Start...");
        UserDetailsDto principal = (UserDetailsDto) authentication.getPrincipal();
        return userService.fetchUserByPrincipal(principal.getUserId())
                .map(userMapper::userDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .header("message", "Logged user not found...")
                        .build());
    }
}
