package com.example.service.simple.jwt.authentication.config;

import com.example.service.simple.jwt.authentication.infrastructure.encryption.TokenParser;
import com.example.service.simple.jwt.authentication.infrastructure.repository.AuthenticationLogRepository;
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

    private final AuthenticationLogRepository authenticationLogRepository;

    public TokenAuthenticationProvider(TokenParser tokenParser,
                                       AuthenticationLogRepository authenticationLogRepository) {
        this.tokenParser = tokenParser;
        this.authenticationLogRepository = authenticationLogRepository;
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("retrieveUser - calling...");
        String token = String.valueOf(authentication.getCredentials());
        try {
            Map<String, Object> claims = tokenParser.parseClaimsAsMap(token);
            return AuthenticatedUser.from(claims);
        } catch (Exception e) {
            throw new CredentialsExpiredException(e.getMessage(), e);
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetails;
        Long authLogId = authenticatedUser.getAuthLogId();
        boolean existValidAuthentication = authenticationLogRepository.existsByIdAndInvalidatedFalse(authLogId);
        if (!existValidAuthentication) {
            throw new CredentialsExpiredException("No valid authentication stored on the database...");
        }
    }
}
