package com.example.service.simple.jwt.authentication.encryption;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_ID;
import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_NAME;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenFactory {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final AppProperties appProperties;

    public TokenFactory(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createNewToken(UserData userData) {
        return Jwts
                .builder()
                .setClaims(buildClaims(userData))
                .signWith(HS256, appProperties.getJwtToken().getSecret())
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    private Claims buildClaims(UserData userData) {
        var userAttributes = Map.of(
                USER_ID, String.valueOf(userData.getId()),
                USER_NAME, userData.getUsername());

        Claims claims = Jwts.claims()
                .setIssuer(appProperties.getName())
                .setIssuedAt(new Date())
                .setExpiration(Timestamp.valueOf(expirationDateAt()));
        claims.putAll(userAttributes);
        return claims;
    }

    private LocalDateTime expirationDateAt() {
        var jwtTokenProperties = appProperties.getJwtToken();
        if (jwtTokenProperties.getExpirationInSeconds() > 0) {
            return LocalDateTime.now()
                    .plusSeconds(jwtTokenProperties.getExpirationInSeconds());
        }
        return LocalDateTime.now()
                .plusSeconds(86400);
    }
}
