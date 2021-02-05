package com.example.service.simple.jwt.authentication.encryption;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_ID;
import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_NAME;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenFactory {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

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

        Date now = new Date();
        Claims claims = Jwts.claims()
                .setIssuer(appProperties.getName())
                .setIssuedAt(now)
                .setExpiration(expirationDateAt());
        claims.putAll(userAttributes);
        return claims;
    }

    private Date expirationDateAt() {
        var jwtTokenProps = appProperties.getJwtToken();
        var expirationDateTime = jwtTokenProps.getExpirationInSeconds() > 0 ?
                LocalDateTime.now().plusSeconds(jwtTokenProps.getExpirationInSeconds()) :
                LocalDateTime.now().plusSeconds(86400);
        return Date.from(expirationDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }
}
