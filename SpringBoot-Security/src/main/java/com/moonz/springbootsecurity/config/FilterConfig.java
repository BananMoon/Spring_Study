package com.moonz.springbootsecurity.config;

import com.moonz.springbootsecurity.filter.AuthorizationCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 커스텀 필터를 직접 등록 & 필터 환경 설정
 * History : [2022-02-22]
 */
//@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<AuthorizationCheckFilter> authenticationFilterRegistrationBean() {
        FilterRegistrationBean<AuthorizationCheckFilter> filterRegistrationBean = new FilterRegistrationBean<>(new AuthorizationCheckFilter());
        filterRegistrationBean.addUrlPatterns("/*");        // 해당 필터를 실행할 url
        filterRegistrationBean.setOrder(0); // 필터가 실행될 우선순위 설정 (낮을수록 높은 우선순위)
        return filterRegistrationBean;
    }
}
