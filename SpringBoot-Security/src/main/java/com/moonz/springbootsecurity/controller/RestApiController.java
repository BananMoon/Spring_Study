package com.moonz.springbootsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API 컨트롤러
 * History : [2022-02-22]
 */
@RestController
public class RestApiController {
    @GetMapping("home")
    public String home() {
        return "<h1>Home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>Token</h1>";
    }

}
