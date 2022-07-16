package com.moonz.securitypractice.security;

import com.moonz.securitypractice.entity.Member;
import com.moonz.securitypractice.v1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * DB에서 요청 유저의 인증 정보를 가져온다.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    // 인증 과정에서 username(아이디)에 해당하는 유저를 DB에서 조회
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("로그: CustomUserDetailsService.loadUserByUsername 실행!!!");

        UserDetailsImpl user = findSecurityUserByUsername(username);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), "", user.getAuthorities());

        /*
        db 내 비밀번호 자체가 인코딩 되어있을듯.
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                passwordEncoder.encode(user.getPassword()), user.getAuthorities());
         */
    }

    // security user 조회
    public UserDetailsImpl findSecurityUserByUsername(String username) {
        // TODO: 2022-07-15 error throw
        Member member = memberRepository.findOneByUsername(username)
                .orElseThrow();
        return convertToUserDetails(member);
    }

    private UserDetailsImpl convertToUserDetails(Member member) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().getFullName()));

        return new UserDetailsImpl(
                member.getUsername(),
                member.getPassword(),
                grantedAuthorities
        );
    }

}