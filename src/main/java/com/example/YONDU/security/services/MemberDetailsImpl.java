package com.example.YONDU.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.YONDU.models.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MemberDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String identifier;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public MemberDetailsImpl(String identifier, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.identifier = identifier;
        this.password = password;
        this.authorities = authorities;
    }

    public static MemberDetailsImpl build(Member member) {
        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new MemberDetailsImpl(
                member.getIdentifier(),
                member.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getPassword() {
        return password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MemberDetailsImpl member = (MemberDetailsImpl) o;
        return Objects.equals(identifier, member.identifier);
    }
}
