package com.example.service.simple.jwt.authentication.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;

    private JwtTokenProperties jwtToken;

    @Getter
    @Setter
    public static class JwtTokenProperties {

        private String secret;

        private long expirationInSeconds;

        private long clockSkewInSeconds;

    }

}
