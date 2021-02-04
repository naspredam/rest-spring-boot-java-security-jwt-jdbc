package com.example.service.simple.jwt.authentication.config;

import com.example.service.simple.jwt.authentication.config.authentication.TokenAuthenticationFilter;
import com.example.service.simple.jwt.authentication.config.authentication.TokenAuthenticationProvider;
import com.example.service.simple.jwt.authentication.encryption.TokenParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher AUTHENTICATION_FREE_MATCHERS = new OrRequestMatcher(
            new AntPathRequestMatcher("/login")
    );

    private static final RequestMatcher AUTHENTICATION_MATCHERS = new NegatedRequestMatcher(AUTHENTICATION_FREE_MATCHERS);

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    private final TokenParser tokenParser;

    SecurityConfig(TokenAuthenticationProvider tokenAuthenticationProvider,
                   TokenParser tokenParser) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
        this.tokenParser = tokenParser;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("********* LOADING SECURITY CONFIGURATION *********");
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(tokenAuthenticationProvider)
                    .addFilterBefore(tokenAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                    .authorizeRequests()
                        .requestMatchers(AUTHENTICATION_MATCHERS).authenticated()
                .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable();
    }

    private TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(AUTHENTICATION_MATCHERS, tokenParser);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((request, response, url) -> { /* do nothing */ });
        return successHandler;
    }
}
