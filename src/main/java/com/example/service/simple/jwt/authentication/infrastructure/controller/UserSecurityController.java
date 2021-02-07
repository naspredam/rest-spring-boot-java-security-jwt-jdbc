package com.example.service.simple.jwt.authentication.infrastructure.controller;

import com.example.service.simple.jwt.authentication.application.usecase.LoginUserUseCase;
import com.example.service.simple.jwt.authentication.application.usecase.LogoutUserUseCase;
import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.LoginRequestBodyDto;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
class UserSecurityController {

    private final LoginUserUseCase loginUserUseCase;

    private final LogoutUserUseCase logoutUserUseCase;

    UserSecurityController(LoginUserUseCase loginUserUseCase,
                                  LogoutUserUseCase logoutUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
        this.logoutUserUseCase = logoutUserUseCase;
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestBodyDto bodyDto) {
        log.info("start logging...");
        String username = bodyDto.getUsername();
        String password = bodyDto.getPassword();
        UserLoginInfo userLoginInfo = UserLoginInfo.of(username, password);
        return loginUserUseCase.createAuthenticationToken(userLoginInfo)
                .map(LoginResponseDto::of)
                .map(dto -> dto.add(linkTo(methodOn(UserController.class).retrieveMyUserInformation()).withSelfRel()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("message", "User and password incorrect...")
                        .build());
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout() {
        logoutUserUseCase.invalidateSession();
    }
}
