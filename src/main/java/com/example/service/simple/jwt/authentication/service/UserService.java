package com.example.service.simple.jwt.authentication.service;

import com.example.service.simple.jwt.authentication.model.UserPrincipal;
import com.example.service.simple.jwt.authentication.repository.UserRepository;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserData> fetchByUserNameAndPwd(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(userData -> passwordEncoder.matches(rawPassword, userData.getPassword()));
    }

    public UserPrincipal fetchUserDetails(String username) {
        return userRepository.findByUsername(username)
                .map(userData -> new UserPrincipal(userData.getId(), userData.getUsername()))
                .orElseThrow(EntityNotFoundException::new);
    }
}
