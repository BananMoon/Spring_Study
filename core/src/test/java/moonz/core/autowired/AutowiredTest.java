package moonz.core.autowired;

import moonz.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

// 스프링 빈이 없을 때에도 문제되지 않도록 처리하는 방법
public class AutowiredTest {
    @Test
    void AutowiredOption() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class); // 스프링 빈으로 등록하는 메서드
    }
    static class TestBean {
        @Autowired(required = false)    // 자동 주입될 대상이 없으면 메서드 자체가 호출이 안됨.
        public void setNoBean1(Member noBean1) {    // 스프링 빈이 아닌 java 객체를 넣는 것이므로 등록될 수 있는 빈이 없는 상태
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {  // 호출은 되도록 하여 null값이라도 넣어주고 싶을 경우
            System.out.println("noBean2= " + noBean2);
        }

        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3= " + noBean3);
        }
    }

}
