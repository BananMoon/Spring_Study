## 10. JSON
> 모든 사람이 메시지를 전송 시, 자기 나라 언어를 영어로 변역하고, 응답받는 사람은 영어를 자기나라 언어로 번역하는 방식이 가장 효율적이다.

> 자바에서는 Java Object 데이터를 보내고, 자바 언어가 데이터를 받을 경우에는 상호 이해 가능하지만, 자바 object를 파이썬에서 받으면 이해하지 못한다.

이때는 어떻게 해야할까?<br>
Java Object를 **JSON**으로 바꿔 전송하고, 응답 시에 JSON을 Python Object로 바꾸는 방식이 효율적이다.
- JSON은 **중간 데이터**가 되는 것.
- `@Controller`에게 Body에 데이터를 담아 request 할 때 JSON 형식으로 보내야 한다. 즉, MIME 타입이 JSON.
  - response 시 에는, Java Object를 JSON 데이터로 바꿔 응답한다.

<hr><br>

## 11. ENUM 클래스
블로그 프로젝트를 진행하던 중, User와 Admin 역할의 필드가 들어갈 role 칼럼을 생성해야 했다. 처음 User 엔티티에 생성한 role칼럼은 다음과 같다.
```java
    @ColumnDefault("'user'")    // 값 없을 시, 'user' 로 초기화
    private String role;   
```

이후에, JPA를 이용하여 데이터를 입력하는 과정을 진행할 때 문제가 발생했다. **role 칼럼에 null값으로 어떤 값도 들어가지 않은 것**이다.<br>
데이터 입력 요청 API는 다음과 같다.
```java
    // http://localhost:8000/blog/dummy/join (요청)
    // body에는 username, password, email
    @PostMapping("/dummy/join")
    public String join(User user) {
        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
        System.out.println("email: " + user.getEmail());
        // System.out.println("id: " + user.getId());   -> null
        // System.out.println("createdAt: " + user.getCreatedAt());   -> null
        // System.out.println("role: " + user.getRole());   -> null

        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
```
전체 칼럼은 `id`, `username`, `password`, `email`, `createdAt`, `role`이다. 여기서 username, password, email만 값을 전달해야하고 id, creatdAt, role은 값을 지정해주지 않았다. 즉 위의 주석처럼 Java 객체에서는 null값이 들어간다. <br>하지만!
- id는 auto_increment이기 때문에 테이블에서 자동 증가&생성된다.
- createdAt은 `@CreationTimestamp`로 인해 시간이 자동 입력된다.
- role은 `@ColumnDefault("'user'")`로 해놨는데... 왜 null값일까?<br>
이유는 (자바 객체에서) role칼럼의 null값이 그대로 MySQL **테이블에도 null값으로 저장**되기 때문에 default값으로 초기화 되지 않은 것이다.<br>
<br>

> 해결 방안

이를 해결하기 위해,
1. `@DynamicInsert` 를 User 엔티티에 붙여주면 JPA에서 처리할 때, null인 값은 제외시키고 값을 insert한다.
- 이런 annotation을 많이 활용하는 것은 편리하지만 그렇게 도움되는 방안은 아니다. 편리함만 늘어날 뿐..
2. API에서 user 객체를 저장하기 전, `user.setRole('user');` 를 통해 추가로 역할을 설정해준다.
- 개발자들이 작성하다 오타가 발생할 수 있다. (예-users, User, USer) 
3. model 폴더에 새로운 Enum 클래스를 따로 생성해준다.
```java
/* RoleType 이라는 Enum 클래스 */
public Enum RoleType {
    USER, ADMIN // USER와 ADMIN만 허용된다. (도메인 제한)
}
```
- role 설정 시에는, `user.setRole(RoleType.USER)` 로 입력하여 오타를 피할 수 있다.
- 또한, DB에는 `RoleType`이라는 클래스개념이 없으므로 해당 role 칼럼에 `@Enumerated(EnumType.STRING)` 를 붙여주어 String으로 알려준다.
<br>


최종 User 엔티티의 role은 다음과 같이 선언했다.
```java
    //@ColumnDefault("'user'")    // 값 없을 시, 'user' 로 초기화
    @Enumerated(EnumType.STRING)
    private RoleType role;   
```