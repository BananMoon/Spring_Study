package com.moonz.securitypractice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
/**
 * UsernamePasswordAuthentication 유형의 인증을 수행 후 인증된 객체 반환한다.
 * 특별한 비밀번호 인코딩을 추가하길 원한다면, AuthenticationProvider를 구현.
 * 그럴 경우, UserDetailsService는 단지 DB에서 정보를 가져오는 역할만 수행함.
 */
@RequiredArgsConstructor
@Component  // CustomJwtAuthenticationProvider
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    /**
     * 요청한 유저의 Authentication이 인증된 유저인지 확인한 후 인증 객체 반환
     * @param authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("CustomAuthenticationProvider.authenticate 실행!!!!");
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        System.out.println("authentication의 name:"+ username);
        System.out.println("authentication의 credentials:"+ password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!userDetails.isEnabled()) {
            throw new UsernameNotFoundException("Error: " + username);
        }
        confirmPasswordMatching(userDetails, (UsernamePasswordAuthenticationToken)authentication);

        // 인증처리된!!!
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    private void confirmPasswordMatching(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() == null) {
            // TODO: 2022-07-14 예외 처리
        }
        String requestedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(requestedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Error: " + userDetails.getUsername());
        }
    }

    /**
     * authentication의 타입이 UserNamePasswordAuthenticationToken인 경우에만 지원하는 필터임을 명시한다.
     * isAssignableForm() : 클래스 유형의 개체를 이 클래스의 개체에 할당할 수 있는지 여부를 나타냅니다.
     * - 나타낼 수 있다면(본인 타입, 혹은 부모 타입) true
     * - 나타낼 수 없다면 false
     * @param authentication
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}