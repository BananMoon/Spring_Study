package com.moonz.securitypractice.v1.dto;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Pattern;
// 내부 클래스(static)에는 인자에 final 붙이면 안되는 이유?
// 한번 null로 생성한 후에 setter를 이용해서 설정하나? final은 한번 결정되면 ..변경x..?

public class MemberRequestDto {
    @Data
    public static class SignUp {
        @NotEmpty(message = "아이디는 필수 입력값입니다.")
        private String username;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String  password;

        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "유효한 이메일을 입력해야합니다.")
        private String email;

        @NotEmpty(message = "성함은 필수 입력값입니다.")
        private String name;

    }
}
