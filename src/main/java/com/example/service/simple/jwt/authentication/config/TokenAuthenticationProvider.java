package com.example.service.simple.jwt.authentication.config;

import com.example.service.simple.jwt.authentication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserService userService;

    public TokenAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("additionalAuthenticationChecks - calling...");
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("retrieveUser - calling...");
        return userService.fetchUserDetails(username);
    }
}
