package com.moonz.securitypractice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Class       : CustomUserDetails
 * Author      : 문 윤지
 * Description : UserDetails 구현체
 * History     : [2022-07-15] 문윤지 - Class Create
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<GrantedAuthority> authorities; // 구현체 : Role.java

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * authorities 필드를 String 타입으로 변환하여 반환한다.
     * @return authorities 값을 콤마로 연결
     */
    public String convertAuthoritiesToString() {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    @Override
    public String getPassword() {
        return password;
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
}