package com.example.service.simple.jwt.authentication.application.service;

import com.example.service.simple.jwt.authentication.application.usecase.LoginUserUseCase;
import com.example.service.simple.jwt.authentication.application.usecase.LogoutUserUseCase;
import com.example.service.simple.jwt.authentication.config.AuthenticatedUser;
import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;
import com.example.service.simple.jwt.authentication.infrastructure.encryption.TokenProvider;
import com.example.service.simple.jwt.authentication.infrastructure.repository.AuthenticationLogRepository;
import com.example.service.simple.jwt.authentication.infrastructure.repository.UserRepository;
import com.example.service.simple.jwt.authentication.infrastructure.repository.data.AuthenticationLogData;
import com.example.service.simple.jwt.authentication.infrastructure.repository.data.UserData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserAuthenticationService implements LoginUserUseCase, LogoutUserUseCase {

    private final UserRepository userRepository;

    private final AuthenticationLogRepository authenticationLogRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    public UserAuthenticationService(UserRepository userRepository,
                                     AuthenticationLogRepository authenticationLogRepository,
                                     PasswordEncoder passwordEncoder,
                                     TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.authenticationLogRepository = authenticationLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Optional<String> createAuthenticationToken(UserLoginInfo userLoginInfo) {
        return userRepository.findByUsername(userLoginInfo.getUsername())
                .filter(userData -> passwordEncoder.matches(userLoginInfo.getPassword(), userData.getPassword()))
                .map(this::logNewAuthentication);
    }

    @Override
    public void invalidateSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        authenticationLogRepository.findById(authenticatedUser.getAuthLogId())
                .map(logData -> logData.toBuilder().invalidated(true).build())
                .map(authenticationLogRepository::save);
    }

    public String logNewAuthentication(UserData userData) {
        LocalDateTime newExpirationDateTime = tokenProvider.newTokenExpirationDateTime();
        Long userId = userData.getId();
        AuthenticationLogData logData = AuthenticationLogData.builder()
                .userId(userId)
                .expirationDateTime(newExpirationDateTime)
                .invalidated(false)
                .build();
        AuthenticationLogData persistedLogData = authenticationLogRepository.save(logData);

        String token = tokenProvider.createNewToken(persistedLogData.getId(), userId, newExpirationDateTime);
        authenticationLogRepository.save(persistedLogData.toBuilder().token(token).build());
        return token;
    }
}
