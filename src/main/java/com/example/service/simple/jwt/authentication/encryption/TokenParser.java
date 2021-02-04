package com.example.service.simple.jwt.authentication.encryption;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import com.example.service.simple.jwt.authentication.repository.data.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_ID;
import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_NAME;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenParser {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final AppProperties appProperties;

    public TokenParser(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public Map<String, String> parseClaimsAsMap(String token) {
        AppProperties.JwtTokenProperties jwtToken = appProperties.getJwtToken();
        Clock clock = Date::new;
        JwtParser jwtParser = Jwts.parser()
                .requireIssuer(appProperties.getName())
                .setClock(clock)
                .setAllowedClockSkewSeconds(jwtToken.getClockSkewInSeconds())
                .setSigningKey(jwtToken.getSecret());
        return jwtParser.parseClaimsJws(token).getBody()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          entry -> String.valueOf(entry.getValue())));

    }

}
