package com.moonz.blog.test;

import com.moonz.blog.model.RoleType;
import com.moonz.blog.model.User;
import com.moonz.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController     // html이 아닌 데이터를 리턴해주는 Controller
public class DummyControllerTest {

    @Autowired      // DI(의존성 주입
    private UserRepository userRepository;
    //@AutoWired : UserRepository 타입으로 Spring이 관리하고 있는게 있다면 userRepository에 값을 넣어라.
    // UserRepository가 메모리에 떠있으므로 주입해줄것임

    // http://localhost:8000/blog/dummy/join (요청)
    // body에는 username, password, email
    @PostMapping("/dummy/join")
    public String join(User user) {

        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
        System.out.println("email: " + user.getEmail());

        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
    //public String join(String username, String password, String email) {    // key=value (약속된 규칙)
    //user를 저장할 때 java 객체에서는 id, role, createdAt 칼럼은 null값이지만
    // id는 auto_increment로 저장되고 createdAt은 Spring에 의해 자동으로 StampTime이 찍힌다.
    // role은 default를 'user'로 설정해두었는데 null값으로 insert되기 때문에 default가 아닌 null로 데이터가 들어가는 것이다.
    // 해결) @DynamicInsert 를 엔티티에 붙여주면 JPA에서 null인 값은 제외시키고 값을 insert한다.
    // But) 이런 식으로 annotation을 많이 붙이는 건 좋지 않다. 그러므로 붙이지 않고, save()하기 전에 role을 지정해준다.
    // user.setRole("user");
    // But2) 개발자들이 작성하다 오타가 발생할 수 있다. 그러므로 model 폴더에 새로운 Enum 클래스를 생성해준다.
    // set할 때는 RoleType.USER 로 세팅!

    // 개발자가 넣는 값을 강제하기 위해서는 ENUM을 사용하면 된다!
    // 실수로 인한 오타와 강제성을 부여할 수 있다.
    // 데이터에 도메인(범위)을 만들 때 String보다 Enum을 만들면 된다.
    // String으로 할 경우 남, 녀를 남자, 여자로 적을 수도 있으니..

    /* Select 문 */
    //{id} 주소로 파라미터 전달 받을 수 있음.
    // localhost:8080/blog/dummy/user/3
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable Long id) {
        //

        // .get() : null일 리가 없다고 생각하고 사용할 수 있는 메서드
        // .orElseGet(Supplier<? extends User>) : null이면 객체 하나 넣어서 만들어줘.
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id: "+ id);
            }
        });

        return user;
        // user 객체 = Java Object
        // 요청: Web Browser. 자바 객체를 이해하지 못함.
        // 변환 (웹 브라우저가 이해할 수 있는 데이터인 json으로 ) 필요.
        // Spring Boot에서는응답할 때 MessageConverter가 자동으로 작동하여
        // Java Object 리턴 -> MessageConverter가 Jackson 라이브러리 호출 -> user 오브젝트를 json으로 변환 -> 브라우저에게 응답
        // 그래서 웹 브라우저에서 json 타입으로 데이터가 보이는 것.
    }
}

// 인터페이스는 인스턴스를 생성할 수 없음.
// 생성하려면 익명 클래스를 만들어야 한다.
// findByID()는 여러 메서드로 객체가 없을 때 처리해주는 방법이 있다. 1. .orElseGet()  2. .orElseThrow()  3. .get()
// 1. .orElseGet() -- Supplier를 넣어줘야 한다.
//    new Supplier<User>() 생성과 동시에, get 추상메서드를 오버라이딩해서 빈 객체를 return한다.
/*
        User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
            @Override
            public User get() {
                return null;
            }
        });
 */
// 2. 이외에도 findById는 .orElseThrow()를 통해 에러를 리턴할 수 있다. Throws: //IllegalArgumentException – if id is null.
/*
User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id: "+ id);
            }
        });
 */
/* 결과창 :
{
    id: null,
    username: null,
    password: null,
    email: null,
    role: null,
    createdAt: null
} */
// 3. .get() 메서드는 해당 객체가 없을 경우가 없다 생각할 때 사용하는 메서드. (거의 사용 X)
// 결과창 : No value present (500 Error)로 발생하므로 에러 메시지가 뜨지 않아 어려움.

// Java 1.8에서는 Supplier<제네릭>과 같은 것을 사용하지 않고, 람다식으로 쉽게 작성하도록 제공함.
/*
User user = userRepository.findById(id).orElseThrow(() -> {
    return new Ille("해당 사용자는 없습니다. id : "+ id);
});
 */