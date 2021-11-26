package com.moonz.SpringbootSecurityBasicV1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration  // 메모리에 뜨도록 
@EnableWebSecurity  // 활성화 on -> 아래에서 추가한 스프링 시큐리티 필터가 기본 스프링 필터체인에 등록된다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();  // 비활성화. why?
        // csrf : Cross Site Request Forgery: 의도치 않은 위조요청을 보냈을 때 csrf protection을 적용하면 html에서 csrf 토큰이 포함되어있어야 요청을 받아들이도록 함으로써 위조 요청을 방지한다.
        // Spring Security는 기본적으로 csrf protection을 제공하여 csrf 공격으로부터 방지한다.
        // BUT, 보안 수준 향상시키는 csrf를 disable하는 이유 : non-browser clients 만을 위한 서비스라면 csrf 코드가 필요 없다.(참고: spring security document)
        // why? rest api를 이용하는 서버는 session 기반 인증과 달리 stateless하기 때문에 따로 서버에 인증 정보를 보관하지 않는다.
        // 즉 csrf 공격으로부터 안전하고 매번 api 요청에서 csrf 토큰을 받지 않기 때문에 해당 scrf protection 기능을 diable() 한다.
        //  rest api에서 client가 권한이 필요한 요청을 하려면 필요한 인증정보(OAuth2, jwt토큰)를 요청에 포함시켜야한다.

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    // 해당 주소로 들어오면 인증 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 권한까지 있어야함
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")   //admin 권한이 있어야 접속 가능
                .anyRequest().permitAll();  // 그외요청은 모두 허용
        // 403 error는 접근권한이 없다는 status값!
    }
}
