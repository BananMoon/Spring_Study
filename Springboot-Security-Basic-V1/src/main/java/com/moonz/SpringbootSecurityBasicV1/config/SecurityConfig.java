package com.moonz.SpringbootSecurityBasicV1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  // 활성화 on -> 아래에서 추가한 스프링 시큐리티 필터가 기본 스프링 필터체인에 등록된다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();  // 비활성화
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    // 해당 주소로 들어오면 인증 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 권한까지 있어야함
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")   //admin 권한이 있어야 접속 가능
                .anyRequest().permitAll();  // 그외요청은 모두 허용
        // 403 error는 접근권한이 없다는 status값!
    }
}
