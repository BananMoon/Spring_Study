package com.moonz.securitypractice.config;

import com.moonz.securitypractice.security.jwt.JwtAuthenticationFilter;
import com.moonz.securitypractice.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// before : WebSecurityConfigurerAdapter를 구현하는 방법
// after : SecurityFilterChain을 빈으로 등록
@Configuration
@EnableWebSecurity  // Spring Security를 활성화해서 기본 스프링 필터체인에 등록시킴. SpringSecurityFilterChain을 자동 상속
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
//    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
//                .authenticationProvider(authenticationProvider)
//                .userDetailsService()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests(authz -> authz
                                .antMatchers("api/v1/user/login").authenticated()
                                .antMatchers("api/v1/user/reissue").authenticated()
                                .antMatchers("api/v1/user/signup").permitAll()
                                .antMatchers("인증필요한요청api").hasAnyRole("USER", "ADMIN")
//                        .access("hasRole('ROLE_USER')") // 특정 권한을 가지는 사용자만 접근 o
                                .antMatchers("/h2-console/**").permitAll()
//                                .antMatchers("/api/v1/user/signup").permitAll()
                                .anyRequest().permitAll()   // 나머지 요청은 모두 가능
                .and()
                                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                )
                .build();
    }

}
