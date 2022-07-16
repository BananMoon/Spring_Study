package com.moonz.securitypractice.v1.service;

import com.moonz.securitypractice.common.Role;
import com.moonz.securitypractice.entity.Member;
import com.moonz.securitypractice.security.jwt.JwtTokenProvider;
import com.moonz.securitypractice.v1.dto.MemberRequestDto;
import com.moonz.securitypractice.v1.dto.MemberResponseDto;
import com.moonz.securitypractice.v1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
//    private final ApiResponse response;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto signUp(MemberRequestDto.SignUp signUpRequestDto) {
        // 1. is duplicate
        if (memberRepository.existsByUsername(signUpRequestDto.getUsername())) {
//            return response.fail("이미 회원가입된 이메일입니다.", {상태코드});
            System.out.println("이미 회원 존재");
        }
        Member member = Member.builder().
                username(signUpRequestDto.getUsername())
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .role(Role.USER)
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .build();

//                .password(psswordEncoder.encode(signUp.getPassword()))
//                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
        System.out.println(member.getUsername());
        Member saveMember = memberRepository.save(member);
        // 2.
        System.out.println("로그: db 저장 완료!");
//        return response.success("회원가입 성공");
        return new MemberResponseDto(saveMember.getId(), saveMember.getUsername(), saveMember.getEmail(), saveMember.getName());
    }
}
