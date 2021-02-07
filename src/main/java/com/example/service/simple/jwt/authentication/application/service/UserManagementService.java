package com.example.service.simple.jwt.authentication.application.service;

import com.example.service.simple.jwt.authentication.application.usecase.CreateNewUserUseCase;
import com.example.service.simple.jwt.authentication.application.usecase.FetchUserDetailsUseCase;
import com.example.service.simple.jwt.authentication.config.AuthenticatedUser;
import com.example.service.simple.jwt.authentication.domain.User;
import com.example.service.simple.jwt.authentication.domain.UserLoginInfo;
import com.example.service.simple.jwt.authentication.infrastructure.mapper.UserPersistenceMapper;
import com.example.service.simple.jwt.authentication.infrastructure.repository.UserRepository;
import com.example.service.simple.jwt.authentication.infrastructure.repository.data.UserData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class UserManagementService implements CreateNewUserUseCase, FetchUserDetailsUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserPersistenceMapper userPersistenceMapper;

    UserManagementService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 UserPersistenceMapper userPersistenceMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public User createUser(User user) {
        String encodedPwd = passwordEncoder.encode(user.getLoginInfo().getPassword());
        User userWithEncodedPwd = user.toBuilder()
                .loginInfo(UserLoginInfo.of(user.getLoginInfo().getUsername(), encodedPwd))
                .build();
        UserData userData = userPersistenceMapper.newData(userWithEncodedPwd);
        UserData savedUserData = userRepository.save(userData);
        return userPersistenceMapper.domain(savedUserData);
    }

    @Override
    public Optional<User> fetchLoggedUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        return userRepository.findById(principal.getUserId())
                .map(userPersistenceMapper::domain);
    }
}
