package com.moonz.springbootsecurity.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.moonz.springbootsecurity.filter.AuthorizationCheckFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration  // Security 설정이 컨테이너에 로드
@EnableWebSecurity  // 활성화 on -> 아래에서 추가한 스프링 시큐리티 필터가 기본 스프링 필터체인에 등록된다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfigurationSource corsConfigurationSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
// 1. csrf, 폼 로그인, httpBasic 방식, 세션 생성 비활성화 ====================
        http
                .csrf().disable()
        // csrf : Cross Site Request Forgery: 의도치 않은 위조요청을 보냈을 때 csrf protection을 적용하면 html에서 csrf 토큰이 포함되어있어야 요청을 받아들이도록 함으로써 위조 요청을 방지한다.
        // Spring Security는 기본적으로 csrf protection을 제공하여 csrf 공격으로부터 방지한다.
        // BUT, 보안 수준 향상시키는 csrf를 disable하는 이유 : non-browser clients 만을 위한 서비스라면 csrf 코드가 필요 없기때문(참고: spring security document)
        // why? rest api로 연결되는 서버는 따로 서버에 인증 정보를 보관하지 않는다.
        // 즉 csrf 공격으로부터 안전하고 매번 api 요청에서 csrf 토큰을 받지 않기 때문에 해당 scrf protection 기능을 diable() 한다.
        //  rest api에서 client가 권한이 필요한 요청을 하려면 필요한 인증정보(OAuth2, jwt토큰)를 요청에 포함시키면 된다.
                .formLogin().disable()  // JWT로 정보 전달할 거라 폼 로그인으로 정보 전달 X
                .httpBasic().disable() // 기본적인 http 방식 사용 X
        // 세션대신 jwt 사용할 것이므로 STATELESS 설정 (Spring Security는 HttpSession을 만들지 않고, SecurityContext를 얻기위해 세션을 사용하지 않을 것임)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

// 2. CORS 정책, url 별 접근권한 설정 ========================
                .cors().configurationSource(corsConfigurationSource)  // cors 정책에 적용=> 1개 ip만 접근 허용!
           .and()
                // URI 접근에 대한 권한 인가
                .authorizeRequests()
                .antMatchers("/api/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 해당 주소로 들어오면 인증 필요
                .antMatchers("/api/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 권한까지 있어야함
                .antMatchers("/api/admin/**").access("hasRole('ROLE_ADMIN')")   //admin 권한이 있어야 접속 가능
                .anyRequest().permitAll()  // 그외 요청은 모두 허용
        // 403 error : 접근권한이 없다! (type=Forbidden)
        // 404 error는 인증이 필요없지만 api 찾지 못함 (type=Not Found)

// 3. Security Filter에 필터 추가 ========================
        .and()
                .addFilterBefore(new AuthorizationCheckFilter(), SecurityContextPersistenceFilter.class);   // 1번 방법
        // 1. Security Filter가 있기 때문에 addFilter로 등록할 수 없고, Security Filter 전, 혹은 후에 거치도록 등록해야한다.
        // 1-1. 만약 Security Filter를 아예 거치기 전에 실행시키려면 SecurityContextPersistenceFilter.class 로 설정한다.
        // 2. Security Filter 중에 등록하지 않거나 여러 필터를 등록하면서 우선순위를 등록하는 방법 - FilterConfig 클래스! 단, Security Filter Chain보다 늦게 실행된다.
    }
}

// 참고
// authentication: 인증은 사용자의 신원을 인식하는 프로세스   ( 사용자를 검증한 뒤 jwt 토큰 생성)
// authorization : 누군가에게 무언가를 하거나 가질 수 있는 권한을 부여하는 프로세스 ( : jwt 토큰을 검증)