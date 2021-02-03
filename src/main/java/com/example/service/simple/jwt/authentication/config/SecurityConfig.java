package com.example.service.simple.jwt.authentication.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
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

    SecurityConfig(TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(AUTHENTICATION_MATCHERS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());

        log.info("********* LOADING SECURITY CONFIGURATION *********");
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .and()
                    .authenticationProvider(tokenAuthenticationProvider)
                    .addFilterBefore(filter, AnonymousAuthenticationFilter.class)
                    .authorizeRequests().requestMatchers(AUTHENTICATION_MATCHERS).authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((request, response, url) -> { /* do nothing */ });
        return successHandler;
    }
}
