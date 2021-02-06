package com.example.service.simple.jwt.authentication.infrastructure.controller;

import com.example.service.simple.jwt.authentication.application.usecase.CreateNewUserUseCase;
import com.example.service.simple.jwt.authentication.application.usecase.FetchUserDetailsUseCase;
import com.example.service.simple.jwt.authentication.domain.User;
import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.CreateUserBodyDto;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.UserProfileDto;
import com.example.service.simple.jwt.authentication.infrastructure.mapper.UserPresentationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserController {

    private final CreateNewUserUseCase createNewUserUseCase;

    private final FetchUserDetailsUseCase fetchUserDetailsUseCase;

    private final UserPresentationMapper userPresentationMapper;

    public UserController(CreateNewUserUseCase createNewUserUseCase,
                          FetchUserDetailsUseCase fetchUserDetailsUseCase,
                          UserPresentationMapper userPresentationMapper) {
        this.createNewUserUseCase = createNewUserUseCase;
        this.fetchUserDetailsUseCase = fetchUserDetailsUseCase;
        this.userPresentationMapper = userPresentationMapper;
    }

    @PostMapping("/users")
    public ResponseEntity<UserProfileDto> createUser(@RequestBody @Valid CreateUserBodyDto bodyDto) {
        User userFromRequest = userPresentationMapper.domainUser(bodyDto);
        User user = createNewUserUseCase.createUser(userFromRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userPresentationMapper.presentationUserDetails(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> retrieveMyUserInformation() {
        log.info("retrieveMyUserInformation - Start...");
        return fetchUserDetailsUseCase.fetchLoggedUserData()
                .map(userPresentationMapper::presentationUserDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .header("message", "Logged user not found...")
                        .build());
    }
}
