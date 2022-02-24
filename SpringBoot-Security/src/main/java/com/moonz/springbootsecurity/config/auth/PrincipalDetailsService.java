package com.moonz.springbootsecurity.config.auth;

import com.moonz.springbootsecurity.model.User;
import com.moonz.springbootsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티는 로그인 요청이 오면 자동으로 UserDetailsService 타입으로 컨테이너에 로드되어 있는 PrincipalDetailsService의 loadUserByUsername 메서드가 실행된다.
 * 사실 직접 UserDetailsService 타입을 구현할 필요 없이, (PrincipalDetails)UserDetailsService.loadUserByUsername(username);  해도 되긴함..
 *
 * User 정보를 담은 PrincipalDetails (UserDetails 타입) 이 반환되면 Authentication 내부에 저장되고,
 * 해당 Authentication 타입은 Security 세션 내부에 저장된다.
 */
// 시큐리티의 로그인 요청 주소가 default로 '/login' 인데 SecurityConfig에서 formLogin()을 disable 해놨기 때문에 Filter로 걸어주거나 Controller를 생성해줘야 한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService.loadUserByUsername");
        User user = userRepository.findByUsername(username);    // 레포지토리에 메서드 추가하기
        if (user != null)
            return new PrincipalDetails(user);      // UserDetails가 반환되면 Authentication 내부에 들어가게 되고, Authentication이 Security 세션 내부에 저장됨.
        return null;
    }
}
