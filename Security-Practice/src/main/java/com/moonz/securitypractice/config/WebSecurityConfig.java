package com.moonz.securitypractice.config;

import com.moonz.securitypractice.security.CustomAccessDeniedHandler;
import com.moonz.securitypractice.security.CustomAuthenticationEntryPoint;
import com.moonz.securitypractice.security.jwt.JwtAuthenticationFilter;
import com.moonz.securitypractice.security.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authenticationProvider(jwtAuthenticationProvider)
                .userDetailsService(userDetailsService)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests(authz -> authz
                                .antMatchers("api/v1/login/**").authenticated()
                                .antMatchers("api/v1/reissue").hasRole("USER")
                                .antMatchers("api/v1/signup").permitAll()
                                .antMatchers("api/v1/logout/**").hasAnyRole("USER", "ADMIN")
//                        .access("hasRole('ROLE_USER')") // 특정 권한을 가지는 사용자만 접근 o
                                .antMatchers("/h2-console/**").permitAll()
                                .anyRequest().permitAll()
                        .and()
                        .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class))
                .build();
    }

}
