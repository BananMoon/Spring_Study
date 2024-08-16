package com.moonz.securitypractice.security;

import com.moonz.securitypractice.member.domain.Member;
import com.moonz.securitypractice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 요청 시도하는 유저의 인증 정보를 조회한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /**
     * UserDetails의 username 에 해당하는 사용자 조회하여 반환한다.
     * @param username the username identifying the user whose data is required.
     * @return 실제 사용자 Security 정보
     * @throws UsernameNotFoundException username에 해당하는 사용자가 없을 경우
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("로그: CustomUserDetailsService.loadUserByUsername 실행!!!");

        return findSecurityUserByUsername(username);
    }

    /**
     * DB에서 username에 해당하는 회원을 Authentication 객체로 만들어 반환한다.
     * @param username 사용자 ID
     * @return UserDetails 주요한 사용자 Security 정보
     * @throws UsernameNotFoundException username에 해당하는 회원 데이터가 없을 경우
     */
    public CustomUserDetails findSecurityUserByUsername(String username) {
        Member member = memberRepository.findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s에 해당하는 회원이 존재하지 않습니다.", username)));
        return convertToUserDetails(member);
    }

    /**
     * 회원 정보로 UserDetails 객체 생성하여 반환한다.
     * @param member 회원
     * @return username, password, authority 세팅된 UserDetails
     */
    private CustomUserDetails convertToUserDetails(Member member) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().getFullName()));

        return new CustomUserDetails(member.getUsername(), member.getPassword(), grantedAuthorities);
    }

}