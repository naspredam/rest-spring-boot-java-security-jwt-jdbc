package com.example.service.simple.jwt.authentication.config.authentication;

import com.example.service.simple.jwt.authentication.encryption.TokenParser;
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

    private final TokenParser tokenParser;

    public TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, TokenParser tokenParser) {
        super(requiresAuthenticationRequestMatcher);
        this.tokenParser = tokenParser;
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

        try {
            UserDetailsDto principalDto = UserDetailsDto.from(tokenParser.parseClaimsAsMap(token));
            Authentication auth = new UsernamePasswordAuthenticationToken(principalDto, token);
            return getAuthenticationManager().authenticate(auth);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw ex;
        }
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
