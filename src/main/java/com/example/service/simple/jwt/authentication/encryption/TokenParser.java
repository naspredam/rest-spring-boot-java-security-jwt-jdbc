package com.example.service.simple.jwt.authentication.encryption;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenParser {

    private final AppProperties appProperties;

    public TokenParser(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public Map<String, String> parseClaimsAsMap(String token) {
        var jwtToken = appProperties.getJwtToken();
        Clock clock = Date::new;
        var jwtParser = Jwts.parser()
                .requireIssuer(appProperties.getName())
                .setClock(clock)
                .setAllowedClockSkewSeconds(jwtToken.getClockSkewInSeconds())
                .setSigningKey(jwtToken.getSecret());

        return jwtParser.parseClaimsJws(token).getBody()
                .entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
                                                      entry -> String.valueOf(entry.getValue())));

    }

}
