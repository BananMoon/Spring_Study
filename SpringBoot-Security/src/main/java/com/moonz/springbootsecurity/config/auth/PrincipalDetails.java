package com.moonz.springbootsecurity.config.auth;

import com.moonz.springbootsecurity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티는 로그인 요청을 처리하고나면 시큐리티 세션을 생성한다. (Security ContextHolder 에)
 * Security ContextHolder에 저장되는 세션 객체의 타입은  Authentication 타입의 객체이어야하는데
 * Authentication 에는 User 정보가 있어야하고,
 * 이 User 객체의 타입은 UserDetails 타입이어햐 한다.
 * <결론>
 * Security Session 영역에 저장될 수 있는 세션 객체는 Authentication 타입이어야하고, 이 Authentication 타입에는 User 객체가 있어야하는데 이 User 객체의 타입은 UserDetails 타입이어야 한다.
 * 우리는 이 UserDetails 타입으로 User 정보를 생성해주야한다.
 * </결론>
 */
// 해당 빈은 UserDetailsService에서 이용할 것이기 때문에 따로 컨텍스트에 Component로 안띄워도 된다.
public class PrincipalDetails implements UserDetails {      // UserDetails를 확장함으로써, PrincipalDetails를 Authentication 객체에 넣을 수 있다.
    private User user;      // 우리의 User 정보

    public PrincipalDetails(User user) {
        this.user = user;
    }

    /**
     * 해당 User의 권한을 리턴한다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        user.getRoles()를 리턴하면 되는데 지정된 반환타입이 있으니 변환해준다.
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRolesList().forEach(r -> {
            authorities.add( () -> r);  // Role을 하나씩 추가
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { // 계정 만료되지않았니? => true : 만료 안됐음.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠기지 않았니?
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 비밀번호 변경 기한이 지나지 않았니?
        return true;
    }

    @Override
    public boolean isEnabled() {            //  휴먼계정이니?
         // 만약 User 클래스에 loginDate(로그인 날짜)가 있다면, 서비스에 1년동안 회원이 로그인하지 않은 경우 휴먼계정으로 젼환되는데,
        // 현재시간 - loginDate > 1년 => return false;
        return true;
    }
}
