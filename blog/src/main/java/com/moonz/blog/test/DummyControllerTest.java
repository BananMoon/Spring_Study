package com.moonz.blog.test;

import com.moonz.blog.model.RoleType;
import com.moonz.blog.model.User;
import com.moonz.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController     // html이 아닌 데이터를 리턴해주는 Controller
public class DummyControllerTest {

    @Autowired      // DI(의존성 주입 : )
    private UserRepository userRepository;
    //@AutoWired : UserRepository 타입으로 Spring이 관리하고 있는게 있다면 userRepository에 값을 넣어라.
    // UserRepository가 메모리에 떠있으므로 주입해줄것임

    // http://localhost:8080/blog/dummy/users
    @GetMapping("/dummy/users")
    public List<User> list() {
        return userRepository.findAll();
    }

    //http://localhost:8080/blog/dummy/users?page=0(1)
    // 한 페이지당 데이터 2건 리턴받을 예정
    @GetMapping("/dummy/user")
    public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        // data 2개씩, id기준으로 내림차순 정렬.
//        Page<User> users = userRepository.findAll( pageable);  -> 전체 Page 리스트를 반환. Content만 띄우려면
        Page<User> pagingUser = userRepository.findAll(pageable);
        // Page 클래스에는 .isFrist(), .isLast() 등 메서드를 이용해서 각 페이지별 분기처리도 가능
        List<User> users = pagingUser.getContent();
        return users;
    }

    // http://localhost:8080/blog/dummy/join (요청)
    // body에는 username, password, email
    @PostMapping("/dummy/join")
    public String join(@RequestBody User user) {         // x-www-url-encoded가 아닌 JSON을 전달받으려면 @RequestBody 추가!
        // MessageConverter 클래스를 구현한 Jackson 라이브러리가 발동하여 json 데이터를 Java Object로 파싱하여 받아준다.
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
        // .get() : null일 리가 없다고 생각하고 사용할 수 있는 메서드
        // .orElseGet(Supplier<? extends User>) : null이면 객체 하나 넣어서 만들어줘.
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 사용자가 없습니다. id: "+ id);
            }
        });
        return user;
        // user 객체 = Java Object
        // 요청: Web Browser. 자바 객체를 이해하지 못함.
        // 변환 (웹 브라우저가 이해할 수 있는 데이터인 json으로 ) 필요.
        // Spring Boot에서는응답할 때 MessageConverter가 자동으로 작동하여
        // Java Object 리턴 -> MessageConverter가 Jackson 라이브러리 호출 -> user 오브젝트를 json으로 변환 -> 브라우저에게 응답
        // 그래서 웹 브라우저에서 json 타입으로 데이터가 보이는 것.

        // 인터페이스는 인스턴스를 생성할 수 없음.  생성하려면 익명 클래스를 만들어야 한다.
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
    return new IllegalArgumentException("해당 사용자는 없습니다. id : "+ id);
});
 */
    }

    // localhost:8080/blog/dummy/user/3
    // email과 password만 수정 가능.
    // JSON으로 데이터 요청-> java Object로 변환(by MessageConerter의 Jackson 라이브러리)하여, @RequestBody로 받을 수 있음.

    @Transactional  // 함수 종료 시 자동 commit된다.
    @PutMapping("dummy/user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User requestUser) {
        System.out.println("id : " + id);
        System.out.println("password : "+ requestUser.getPassword());
        User user = userRepository.findById(id).orElseThrow(()-> {          // 영속화
            return new IllegalArgumentException("수정에 실패하였습니다.");
        }); // null값이 없는 user!

        user.setPassword(requestUser.getPassword());    // 값 변경
        user.setEmail(requestUser.getEmail());
        // userRepository.save(user);  // set된 user정보로 save(). 해당 id를 전달했을 때 id가 존재하면 insert가 아닌 update 진행됨.
        // userRepository.save() 대신 @Transactional
        //  Dirty Checking?
        return user;
    }   // @Transaction이 변경 감지 : 이를 더티 체킹이라 함.

    @DeleteMapping("dummy/delete/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        // id가 없을 경우, 에러가 발생할 것이므로 try-catch로 감싸야한다.
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {    // Exception으로 던져도 되지만, 이럴 경우 모든 에러가 이 Exception에 걸리기 때문에 비추.
            return "삭제에 실패했습니다. 해당 ID는 DB에 없습니다.";
        }
        return "해당 id가 삭제되었습니다. id : "+ id;
    }
}

