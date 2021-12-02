package com.moonz.blog.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    public void 해쉬_암호화() {
        String encPwd = new BCryptPasswordEncoder().encode("1234");
        System.out.println("1234 해쉬: "+ encPwd);
    }
}