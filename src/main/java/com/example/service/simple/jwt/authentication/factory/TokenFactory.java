package com.example.service.simple.jwt.authentication.factory;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenFactory {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final AppProperties appProperties;

    public TokenFactory(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createNewToken(Map<String, String> attributes) {
        Claims claims = Jwts.claims()
                .setIssuer(appProperties.getName())
                .setIssuedAt(new Date());

        AppProperties.JwtTokenProperties jwtTokenProperties = appProperties.getJwtToken();
        if (jwtTokenProperties.getExpirationInSeconds() > 0) {
            LocalDateTime expiresAt = LocalDateTime.now()
                    .plusSeconds(jwtTokenProperties.getExpirationInSeconds());
            claims.setExpiration(Timestamp.valueOf(expiresAt));
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, jwtTokenProperties.getSecret())
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }
}
