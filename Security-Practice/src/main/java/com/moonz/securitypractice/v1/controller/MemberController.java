package com.moonz.securitypractice.v1.controller;

import com.moonz.securitypractice.v1.dto.MemberRequestDto;
import com.moonz.securitypractice.v1.dto.MemberResponseDto;
import com.moonz.securitypractice.v1.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberRequestDto.SignUp signupRequestDto) {
        // 1. argu validation check
        // 2. UserService 호출
        System.out.println("argu validation 통과");
        System.out.println(signupRequestDto.toString());
        System.out.println("requestdto의 id: " + signupRequestDto.getUsername());
        System.out.println("requestdto의 name: " + signupRequestDto.getName());
        System.out.println("requestdto의 email: " + signupRequestDto.getEmail());
        MemberResponseDto memberResponseDto = memberService.signUp(signupRequestDto);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }

}
