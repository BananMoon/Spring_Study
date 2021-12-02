package com.moonz.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    @GetMapping({"", "/"})
    public String index() {
        return "index";   // Controller에서 반환된 String을 중간에 검사하여 기본 경로(src/main/resources/templates)와 확장자를 붙여 해당 경로에 있는 템플릿 파일을 return
    }
}
