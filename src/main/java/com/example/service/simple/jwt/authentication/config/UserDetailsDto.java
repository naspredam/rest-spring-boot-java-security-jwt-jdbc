package com.example.service.simple.jwt.authentication.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_ID;
import static com.example.service.simple.jwt.authentication.encryption.TokenClaimsKeys.USER_NAME;

public class UserDetailsDto implements UserDetails {

    private final Long userId;

    private final String username;

    public UserDetailsDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
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

    public static UserDetailsDto from(Map<String, String> claims) {
        Long userId = Long.parseLong(claims.get(USER_ID));
        String username = claims.get(USER_NAME);
        return new UserDetailsDto(userId, username);
    }
}
