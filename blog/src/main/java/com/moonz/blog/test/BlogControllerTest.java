package com.moonz.blog.test;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 스프링이 com.cos.blog 패키지 이하를 스캔하여 모든 파일을 new하는 것이 아니라
// 특정 어노테이션이 붙어있는 클래스 파일들을 new하여 (IoC) 스프링 컨테이너에  관리해준다.
@RestController
public class BlogControllerTest {

    @GetMapping("/test/hello")
    public String hello() {
        return "<h1>hello spring boot</h1>";
    }
}
