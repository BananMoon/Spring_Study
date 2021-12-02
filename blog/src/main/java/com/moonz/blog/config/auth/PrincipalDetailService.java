package com.moonz.blog.config.auth;

import com.moonz.blog.model.User;
import com.moonz.blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 만약 PrincipalDetail이 UserDetailsService의 메서드를 오버라이딩해서 findByUsername으로 유저정보를 반환하지 않으면
// 우리가 들고 있는 유저 정보를 담아줄 수 없다. just 해당 유저 id, pwd는 db의 유저정보가 아닌 기본 id: user, pwd : 콘솔창에 뜨는 비밀번호로
// UserDetailService를 구현해서 해당 메서드에서 PrincipalDetail 오브젝트로 생성해서 세션에 저장하도록
// Principal
@Service
public class PrincipalDetailService implements UserDetailsService {
    private UserRepository userRepository;
    public PrincipalDetailService(UserRepository userRepository) {  // 생성자 주입
        this.userRepository = userRepository;
    }

    @Override   // 스프링이 로그인 요청을 가로챌 때 username, password 두 변수를 가로채는데 pwd 처리는 알아서.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   //username이 DB에 있는지만 확인!
        User principal = userRepository.findByUsername(username)    // user 객체를 principal에 저장
                .orElseThrow(()-> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
                });
        // 있으면 user 오브젝트가 리턴되서 principal에 저장
        // user 오브젝트를 UserDetails 타입의 PrincipalDetail로 세션에 저장해야하므로 인스턴스 생성
        return new PrincipalDetail(principal);  // PrincipalDetail에 파라미터1개 있는 생성자 추가해야함~
        // 리턴하면 SecurityConfig의 configure에서 받은 user값을 암호화해서 pwd값을 한번더 비교.
        // 비교 후에 시큐리티 세션(영역)에 유저정보가 저장됨
    }
}
