package com.example.service.simple.jwt.authentication.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.example.service.simple.jwt.authentication.infrastructure.encryption.TokenClaimsKeys.AUTH_LOG_ID;
import static com.example.service.simple.jwt.authentication.infrastructure.encryption.TokenClaimsKeys.USER_ID;

public class AuthenticatedUser implements UserDetails {

    private final Long userId;

    private final Long authLogId;

    public AuthenticatedUser(Long authLogId, Long userId) {
        this.authLogId = authLogId;
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getAuthLogId() {
        return authLogId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static AuthenticatedUser from(Map<String, Object> claims) {
        Long userId = Long.valueOf((String) claims.get(USER_ID));
        Long authLogId = Long.valueOf((String) claims.get(AUTH_LOG_ID));
        return new AuthenticatedUser(authLogId, userId);
    }
}
