package com.example.service.simple.jwt.authentication.infrastructure.encryption;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.example.service.simple.jwt.authentication.infrastructure.encryption.TokenClaimsKeys.AUTH_LOG_ID;
import static com.example.service.simple.jwt.authentication.infrastructure.encryption.TokenClaimsKeys.USER_ID;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenProvider {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private final AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createNewToken(Long logSessionId, Long userId, LocalDateTime expirationDateTime) {
        return Jwts
                .builder()
                .setClaims(buildClaims(logSessionId, userId, expirationDateTime))
                .signWith(HS256, appProperties.getJwtToken().getSecret())
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    public LocalDateTime newTokenExpirationDateTime() {
        var jwtTokenProps = appProperties.getJwtToken();
        return jwtTokenProps.getExpirationInSeconds() > 0 ?
                LocalDateTime.now().plusSeconds(jwtTokenProps.getExpirationInSeconds()) :
                LocalDateTime.now().plusSeconds(86400);
    }

    private Claims buildClaims(Long logSessionId, Long userId, LocalDateTime expirationDateTime) {
        Date now = new Date();
        Instant expirationInstant = expirationDateTime.atZone(DEFAULT_ZONE_ID).toInstant();
        Date expirationDate = Date.from(expirationInstant);
        Claims claims = Jwts.claims()
                .setIssuer(appProperties.getName())
                .setIssuedAt(now)
                .setExpiration(expirationDate);

        var userAttributes = Map.of(
                AUTH_LOG_ID, String.valueOf(logSessionId),
                USER_ID, String.valueOf(userId));
        claims.putAll(userAttributes);
        return claims;
    }

}
