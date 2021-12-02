## Security 설정
고려 사항
1. '/auth/**'로 오는 요청은 인증과정 없이 접속 가능하도록 한다.
  - 로그인, 회원가입 화면은 인증과정 없이 들어올 수 있는 화면이므로 해당 api의 uri 앞에 '/auth/'를 추가! 
2. 그외 요청은 모두 인증과정이 필요하므로, 자동으로 '/auth/loginForm'로 이동하도록 한다.


### (1) build.gradle에 의존성 추가
`implementation 'org.springframework.boot:spring-boot-starter-security'`

### (2) 기존에 로그인 구현한 메서드들은 주석처리
Spring Security를 사용할 것이니 직접 구현했던 로그인 메서드를 지워줘도 된다. 
- 예) UserRepository의 `findByUsernameAndPassword()`, UserService의 `로그인()`
<br>


### (3) Spring Security 에 대한 설정 파일 config/SecurityConfig 클래스
[SpringConfig 코드]
```java
@Configuration  // 설정파일은 bean에 등록되어야한다. 빈 등록이란? 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@EnableWebSecurity  // Controller가 가로채기전에 아래에서 url을 체크해야하므로 시큐리티 필터가 등록되어 아래 설정을 먼저 거친다.
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 주소로 접근하면 권한 및 인증을 미리 체크하겠다
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Ctrl+O : Override 가능 메서드목록

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()    // request가 들어오면 보안검사 시작
                    .antMatchers("/auth/**")    // /auth/로 들어오면 누구나 들어올 수 있다.
                    .permitAll()
                    .anyRequest()   // 그외의 모든 요청은
                    .authenticated()  // 보안 검사를 하여 인증이 되어야한다.
                .and()  // '/auth/~~'외엔 다 인증이 필요하니까
                    .formLogin()    //보안 검증은 formLogin방식으로 하겠다.
                    .loginPage("/auth/loginForm");  // 로그인페이지로 이동하도록 한다.
    }
}
```
* WebSecurityConfigurerAdapter : 스프링 시큐리티의 웹 보안 기능 초기화 및 설정하도록 하위클래스가 확장 가능하도록한 추상 클래스
* SpringConfig :security dependency를 추가한 이후 basic한 security를 설정및 구현하는 클래스

## Security에 대하여
1. 로그인 요청 시 전송되는 파라미터(username과 password)를 시큐리티가 가로채서 내부적으로 로그인을 진행한다.
2. 로그인이 완료되면 시큐리티 세션에 user 정보를 등록한다. (IoC)
  - 유저 정보가 필요할 때마다 시큐리티 세션에서 가져와서 DI를 진행한다.

* 시큐리티 세션에는 오브젝트만 저장할 수 있기 때문에 원래 타입인 userDetails를 User object로 바꿔줘야한다. 방법은 **상속**이다. `userDetails extends userObject`를 하면 userDetails는 userDetails와 userObject가 둘다 사용할 수 있다.=>**다형성** (사실 userDetails 인스턴스를 생성할 때마다 상속된 userObject 클래스도 함께 로드된다.)


- 시큐리티는 해쉬암호화되지 않은 비밀번호는 비교불가하기 때문에 모두 해시 암호화를 해야한다.

> 해시란<br>
> 고정 길이의 문자열로 변경된 값인데, 내용이 조금이라도 바뀌면 완전 다른 값이 나옴.
<br>

## csrf와 XSS
**XSS 공격**
Crose Cite Scripting : 자바스크립트 공격<br>
게시판에 글을 작성할 때 글의 제목에 해당 스크립트를 작성하여
```javascript
<script>
    for (var i=0; i<50000;i++)
        alert("안녕");
</script>
```

해당 게시판에서 알람이 50000번 뜨면서 페이지가 마비되도록 하는 것이다.<br>
이에 대한 방어로 lucy-xss를 사용한다. (made by 네이버)

**csrf**
http://naver.com/admin/point?id=attacker&point=50000 : attacker라는 id에 50000 포인트가 쌓이는 url이라고 가정한다.

그럼 시큐리티는 '/admin/**' 으로 들어오는 uri는 권한이 ADMIN 이상일 때만 접속 가능하다.

공격자가 관리자에게 하이퍼링크가 걸린 이미지를 클릭하여 확대해서 봐달라는 메일을 보낸다.
그 이미지의 주소는 다음과 같이 되어있다.<br>
`<a href="http://naver.com/admin/point?id=moon&point=50000"><img src="도라에몽사진.jpg">/<a>` 

관리자는 권한이 ADMIN이기 때문에 해당 url로 인증없이 접속 가능하여 공격자의 의도대로 실행이 된다.

### 방어
1. 하이퍼링크로 공격할 수 없는 Post 방식으로 작성한다.
2. 같은 도메인 상에서 요청이 들어오지 않으면 차단하도록 한다.
3. 로그인, 또는 작업화면 요청 시 CSRF 토큰을 생성하여 세션에 저장하고 사용자의 모든 요청에 대하여 서버단에서 검증한다. -> 요청페이지에 hidden 타입으로 CSRF토큰을 셋팅하여 전송한다.

- Spring Security는 csrf 토큰이 없으면 자동으로 막는다. 그러나 
현재 회원가입할 때 js파일에서 ajax를 통해 요청하기 때문에 토큰을 생성하지 않는다. 그러므로 csrf 토큰을 비활성화(`.csrf().disable()`)시켜 토큰을 생성하지 않아도 막히지 않도록 한다.

