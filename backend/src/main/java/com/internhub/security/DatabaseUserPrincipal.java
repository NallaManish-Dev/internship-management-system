package com.internhub.security;

import com.internhub.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public final class DatabaseUserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    private DatabaseUserPrincipal(Long id, String email, String passwordHash, boolean active, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.active = active;
        this.authorities = authorities;
    }

    public static DatabaseUserPrincipal from(User user) {
        return new DatabaseUserPrincipal(user.getId(), user.getEmail(), user.getPasswordHash(), user.getAccountStatus().name().equals("ACTIVE"), List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    public Long getId() { return id; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return active; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return active; }
}
