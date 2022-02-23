package com.moonz.springbootsecurity.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 인증 및 권한 처리
 * 토큰을 통해 인증 ->
 * authentication(인증) : 사용자의 신원을 인식하는 프로세스   ( 사용자를 검증한 뒤 jwt 토큰 생성할 메서드를 호출한다.)
 */
public class JwtAuthenticationFilter extends  OncePerRequestFilter {
}
