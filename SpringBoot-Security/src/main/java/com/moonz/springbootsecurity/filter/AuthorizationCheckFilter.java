package com.moonz.springbootsecurity.filter;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 토큰 확인
 */
public class AuthenticationFilter extends OncePerRequestFilter {
    String HEADER_AUTH = "Authorization";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("AuthenticationFilter.doFilterInternal 작동! ");
        
        // request의 header를 확인
        String token = request.getHeader(HEADER_AUTH);
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            System.out.println("headerAuth = " + token);
            filterChain.doFilter(request, response);    // Authorization에 토큰이 들어온 경우에만 필터를 이어서 타도록 함.
        } else {
            PrintWriter out = response.getWriter();
            out.println("Authorization 절차 : 인증 X"); // 더 이상 필터 체인이 이어지지 않도록 함. (컨트롤러에도 진입 불가)
        }
        // 사용자 정보(id, pwd)가 들어와서 로그인이 완료되면, 토큰을 생성해서 응답한다.
        // 그후 요청과 함께 온 토큰이 헤더의 Authorization의 value로 들어오면, 유효한 토큰인지 검증한다.

    }
}
