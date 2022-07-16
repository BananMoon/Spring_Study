package com.moonz.securitypractice.security.auth.service;

import com.moonz.securitypractice.entity.Member;
import com.moonz.securitypractice.security.auth.dto.AuthRequestDto;
import com.moonz.securitypractice.security.auth.dto.AuthResponseDto;
import com.moonz.securitypractice.security.jwt.JwtTokenProvider;
import com.moonz.securitypractice.v1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto.TokenInfo login(AuthRequestDto.Login loginRequestDto) {
        // 1. 존재 여부 검증
        // TODO: 2022-07-14 예외 throw
        Member member = memberRepository.findOneByUsername(loginRequestDto.getUsername())
                .orElseThrow(null);

        // 2. 인증(Authentication) 객체 및 검증
        // TODO: 2022-07-16 Security Filter에서 처리할 예정

        // 3.jwt 생성 및 세팅
        AuthResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateTokens(member.getUsername(), member.getRole().getFullName());
        // TODO: 2022-07-14 이미 로그인 되어있는지 체크 로직. 우선 존재여부 상관없이 다시 업데이트.
        member.setRefreshToken(tokenInfo.getRefreshToken());

        // TODO: 2022-07-14 ApiResponseOjbect 생성 후 대체
        return tokenInfo;
    }

    public AuthResponseDto.Reissue reissueAccessToken(AuthRequestDto.Reissue reissueRequestDto) {
        // TODO: 2022-07-15 예외 생성
        Member member = memberRepository.findOneByUsername(reissueRequestDto.getUsername())
                .orElseThrow(null);

        // 1. Refresh Token 유효성 및 일치 체크
        // TODO: 2022-07-16 Filter 내에서 검증 예정
       /* if (!jwtTokenProvider.validateToken(reissueRequestDto.getRefreshToken())
                || !member.getRefreshToken().equals(reissueRequestDto.getRefreshToken())) {
            // TODO: 2022-07-14 Refresh Token 유효 X 예외 생성
        }*/

        // TODO: 2022-07-15 로그아웃된 경우 예외 처리

        // 2. AccessToken 재발급
        AuthResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateTokens(member.getUsername(), member.getRole().getFullName());

        // TODO: 2022-07-15 ApiResponseOjbect 생성 후 대체
        return new AuthResponseDto.Reissue(tokenInfo.getAccessToken());
    }
}
