package com.moonz.SpringbootSecurityBasicV1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view 리턴
public class IndexController {
    @GetMapping({"/", ""})
    public String index() {
        return "index";
        // mustache 기본 설정 : src/
        // 뷰 리졸버 설정(suffix, prefix) 필요없음 (bc mustache 의존성 부여)
    }

    @GetMapping("/user")
    public @ResponseBody String user() { return "user";  }  // @ResponseBody를 붙이면 "user"가 String으로 전달됨
    @GetMapping("/admin")
    public @ResponseBody String admin() { return "admin";  }
    @GetMapping("/manager")
    public @ResponseBody String manager() { return "manager";  }

    // 스프링 시큐리티가 해당 url을 낚아채서 로그인화면으로 바꿈.
    // SecurityConfig클래스에서 http.authorizeRequests()로 접근권한을 설정해주고 나니  security의
    // http.csrf() 작동 안함.
    @GetMapping("/login")
    public @ResponseBody String login() { return "login"; }

    @PostMapping("/join")
    public @ResponseBody String join() {
        return "join";
    }
    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() { return "회원가입 완료됨!"; }

}
