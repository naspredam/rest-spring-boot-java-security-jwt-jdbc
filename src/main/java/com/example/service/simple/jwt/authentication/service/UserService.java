package com.example.service.simple.jwt.authentication.service;

import com.example.service.simple.jwt.authentication.config.UserDetailsDto;
import com.example.service.simple.jwt.authentication.encryption.TokenFactory;
import com.example.service.simple.jwt.authentication.repository.UserRepository;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenFactory tokenFactory;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenFactory tokenFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenFactory = tokenFactory;
    }

    public Optional<String> createTokenByUsernameAndPwd(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(userData -> passwordEncoder.matches(rawPassword, userData.getPassword()))
                .map(tokenFactory::createNewToken);
    }

    public Optional<UserData> fetchLoggedUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDto principal = (UserDetailsDto) authentication.getPrincipal();
        return userRepository.findById(principal.getUserId());
    }

}
