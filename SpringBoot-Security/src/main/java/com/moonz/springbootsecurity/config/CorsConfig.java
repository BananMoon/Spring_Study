package com.moonz.springbootsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration   // 스프링이 관리
public class CorsConfig {
    // Spring cors

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CorsFilter는 Filter를 사용하는 것이어서 Security와 같이 사용할 때 Filter에서 충돌이 발생하는 경우가 있다.
        // 예외 상황을 피하고자 cors의 CorsConfiguration 을 사용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);   // 서버가 응답 시, json을 javascript가 처리(parse)할 수 있도록 설정. 이를 허용하지 않으면??
        config.addAllowedOrigin("http://localhost:8080");   // 해당 ip만 허용
        config.addAllowedHeader("*"); // 모든 헤더에 응답을 허용
        config.addAllowedMethod("*");   //  PUT,GET..  요청 모두 허용
        source.registerCorsConfiguration("/api/**", config);    // 해당 url로 들어오면 위의 설정을 따르도록 설정
        return source;      // UrlBasedCorsConfigurationSource가 CorsConfigurationSource을 상속받으므로.
    }
    // cors에 등록해야 적용이 됨. cors.configurationSource()
    // CorsFilter였다면 필터이므로, addFilter()
}
