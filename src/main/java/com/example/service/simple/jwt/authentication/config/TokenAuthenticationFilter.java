package com.example.service.simple.jwt.authentication.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    public TokenAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("attemptAuthentication - Start...");
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            throw new BadCredentialsException("Missing Authentication Token");
        }

        String token = authHeader.replace(BEARER, "").trim();
        log.info("attemptAuthentication - token = {}", token);

        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.addHeader("login-failure-message", failed.getMessage());
        log.warn("Authentication failed for the call '{}' with message: {} - Returning UNAUTHORIZED(401)",
                request.getRequestURI(), failed.getMessage(), failed.getCause());
    }
}
