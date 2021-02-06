package com.example.service.simple.jwt.authentication.infrastructure.encryption;

import com.example.service.simple.jwt.authentication.config.AppProperties;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class TokenParser {

    private final AppProperties appProperties;

    public TokenParser(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public Map<String, Object> parseClaimsAsMap(String token) {
        var jwtToken = appProperties.getJwtToken();
        Clock clock = Date::new;
        JwtParser jwtParser = Jwts.parser()
                .requireIssuer(appProperties.getName())
                .setClock(clock)
                .setAllowedClockSkewSeconds(jwtToken.getClockSkewInSeconds())
                .setSigningKey(jwtToken.getSecret());

        return jwtParser.parseClaimsJws(token).getBody();
    }

}
