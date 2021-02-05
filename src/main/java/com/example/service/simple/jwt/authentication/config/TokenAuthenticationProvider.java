package com.example.service.simple.jwt.authentication.config;

import com.example.service.simple.jwt.authentication.encryption.TokenParser;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final TokenParser tokenParser;

    public TokenAuthenticationProvider(TokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("retrieveUser - calling...");
        String token = String.valueOf(authentication.getCredentials());
        try {
            Map<String, String> claims = tokenParser.parseClaimsAsMap(token);
            return UserDetailsDto.from(claims);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException(e.getMessage(), e);
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }
}
