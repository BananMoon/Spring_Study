package com.moonz.blog.config;

import com.moonz.blog.config.auth.PrincipalDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration  // 설정파일은 bean에 등록되어야한다. 빈 등록이란? 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@EnableWebSecurity  // Controller가 가로채기전에 아래에서 url을 체크해야하므로 시큐리티 필터가 등록되어 아래 설정을 먼저 거친다.
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 주소로 접근하면 권한 및 인증을 미리 체크하겠다
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Ctrl+O : Override 가능 메서드목록

    private PrincipalDetailService principalDetailService;

    public SecurityConfig(PrincipalDetailService principalDetailService) {
        this.principalDetailService = principalDetailService;
    }

    @Bean   // 해당 메서드가 리턴하는 값을 스피링이 관리해서 필요할 때마다 사용하도록 함(IoC)
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }
    // 시큐리티가 대신 로그인해주게 되면
    // pwd가 어떤 걸로 해쉬화됐는지 알아야
    // 로그인 시 입력한 pwd를 같은 방식으로 해쉬화하여 DB의 pwd 해쉬값과 비교할 수 있다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //  password Encoder가 encodePWD()라는 것을 실제 로그인을 진행하는 ~~에게 알려줘야 한다.
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
        // principalDetailService를 넣음으로써 pwd 비교가 가능해짐짐
   }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   // csrf 토큰 비활성화 (테스트 시 걸어두는게 좋음)
                .authorizeRequests()    // request가 들어오면 보안검사 시작
                    .antMatchers("/auth/**", "/", "/js/**", "/css/**", "/image/**")    // /auth/로 들어오면 누구나 들어올 수 있다.+ /, /js/, /css/, /image/ 등은 들어올 수 있게 해야함.
                    .permitAll()
                    .anyRequest()   // 그외의 모든 요청은
                    .authenticated()  // 보안 검사를 하여 인증이 되어야한다.
                .and()  // '/auth/~~'외 모든 페이지 요청은 다 인증이 필요하니까 loginForm으로 이동
                    .formLogin()    //보안 검증은 formLogin방식으로 하겠다.
                    .loginPage("/auth/loginForm")  // 로그인페이지로 이동하도록 해서 해당 화면에서 로그인 버튼을 클릭하면 아래 url 요청으로 오면
                    .loginProcessingUrl("/auth/loginProc") // the URL(to validate username and password)을 파라미터로 적으면 시큐리티가 로그인 처리를 진행한다. 이때 PrincipalDetailService의 loadUserByUsername로 user 정보를 전달한다.
                    .defaultSuccessUrl("/");    // 정상적으로 로그인 인증이 끝나면 어디로 가는가? 실패시 url 이동 메서드 :   .failureUrl()

        // 세션 등록할 때 UserObject를 등록하면 안되므로 UserDetails를 등록한다.
    }
}
