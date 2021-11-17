package com.moonz.SpringbootSecurityBasicV1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//IoC로 작업하기 위해 @Configuration
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // mustache의 뷰 리졸버를 재설정할 수 있다.
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");    // suffix를 html로 수정
        registry.viewResolver(resolver);    // 뷰 리졸버 등록
    }
}
