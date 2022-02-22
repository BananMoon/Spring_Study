package com.moonz.springbootsecurity.config;

import com.moonz.springbootsecurity.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 필터를 직접 등록할 수 있는 Filter 환경 설정
 */
//@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistrationBean() {
        FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>(new AuthenticationFilter());
        filterRegistrationBean.addUrlPatterns("/*");        // 해당 필터를 실행할 url
        filterRegistrationBean.setOrder(0); // 필터가 실행될 우선순위 설정 (낮을수록 높은 우선순위)
        return filterRegistrationBean;
    }
}
