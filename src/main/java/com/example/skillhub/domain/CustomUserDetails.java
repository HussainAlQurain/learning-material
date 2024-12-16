package com.example.skillhub.domain;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Author author;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming 'role' is stored as a String like "ROLE_USER" or "ROLE_ADMIN"
        return Collections.singleton(new SimpleGrantedAuthority(author.getRole()));
    }

    @Override
    public String getPassword() {
        return author.getPassword();
    }

    @Override
    public String getUsername() {
        return author.getEmail(); // Using email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isEnabled() {
        return "active".equalsIgnoreCase(author.getStatus());
    }

    // Additional getters for Author fields if needed
    public Long getId() {
        return author.getId();
    }

    public String getName() {
        return author.getName();
    }

}
