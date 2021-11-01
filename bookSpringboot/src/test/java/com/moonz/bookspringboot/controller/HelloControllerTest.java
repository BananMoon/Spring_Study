package com.moonz.bookspringboot.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)    //JUnit에 내장된 실행자 외에 다른 실행자 실행!SpringRunner라는 스프링 실행자 사용. 스프링부트 테스트와 JUNit 사이 연결자 역할
@WebMvcTest(controllers = HelloController.class)     //여러 test annotation 중 Web(Spring MVC)에 집중할 수 있는 annotation. 단 @Service, @Component, @Repository 사용 불가. @Controller, @ControllerAdvice 사용 가능
public class HelloControllerTest {
    @Autowired  //스프링이 관리하는 빈(Bean) 주입
    private MockMvc mvc;    //스프링 MVC 테스트의 시작점. 웹 API(GET, POST) 테스트 시 사용

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello = "hello";

        mvc.perform(get("/hello"))  // 체이닝 지원. 여러 검증 기능을 이어서 선언 가능
                .andExpect(status().isOk())
                .andExpect(content().string(hello));    //응답 본문의 내용 검증
    }

    // 추가
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name="hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(name)));
    }
}
