package com.example.cinema.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails { // TODO: 6/25/2022 not really an entity

    private final Spectator spectator;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.spectator.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.spectator.getPassword();
    }

    @Override
    public String getUsername() {
        return this.spectator.getEmail();
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
}
