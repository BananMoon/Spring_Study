package com.moonz.blog.config.auth;

import com.moonz.blog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
// Spring security가 로그인 요청을 가로채서 로그인을 진행하고 완료되면
// UserDetails 타입의 오브젝트(PrincipalDetail)를 스프링 시큐리티의 고유한 세션 저장소에 저장을 한다.
// PrincipalDetail을 저장할 때는 DB에 저장한 User도 포함되어 있어야 한다. (컴포지션)
// 즉! PrincipalDetail은 로그인 진행 후, UserDetails 타입의 오브젝트를 세션 저장소에 저장하는 메서드
public class PrincipalDetail implements UserDetails {   // 인터페이스이므로 오버라이딩
    private User user;  // 객체를 품고 있는 것을 composition이라 한다.

    public PrincipalDetail(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override   // 계정이 만료되지 않았는지 리턴 (true: 만료되지 않음)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override   // 계정이 잠겨있지 않은지 리턴 (true: 잠기지 않음)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override   // 비밀번호가 만료되지 않았는지 리턴 (true: 만료되지 않음)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override   // 계정 활성화 (사용가능)되어있는지 리턴 (true: 활성화)
    public boolean isEnabled() {
        return true;
    }

    @Override   // 계정이 갖고있는 권한 목록(많을 경우 루프) 리턴.
    public Collection<? extends GrantedAuthority> getAuthorities() {    // 리턴 타입이.. GrantedAuthority를 상속한 타입의 Collection이어야함.
        Collection<GrantedAuthority> collectors = new ArrayList<>();    // Collection을 상속받는 ArrayList
        /*
        collectors.add(new GrantedAuthority() { // 인터페이스니까 익명 클래스가 만들어져서 추상 클래스가 오버라이드됨
            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRole();    // spring prefix 규칙 : ROLE_{역할명}
            }
        });
        // 자바는 인자로 메서드를 넘겨줄 수 없기 때문에 GrantedAuthority 객체 생성하고 익명 클래스로 메서드를 오버라이드한 것.
        // collectors.add() 에는 무조건 GrantedAuthority 오브젝트가 들어와야하고, getAuthority 메서드를 오버라이드해야되는 것도 아니까*/
        // 람다 한줄로 바꾸는게 가능
        collectors.add(()-> { return "ROLE_" + user.getRole();});
        return collectors;
    }
}
