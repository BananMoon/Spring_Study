package moonz.core.singleton;

import moonz.core.AppConfig;
import moonz.core.member.MemberRepository;
import moonz.core.member.MemberServiceImpl;
import moonz.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberServiceImpl memberService = ac.getBean(MemberServiceImpl.class); // 방금 MemberServiceImpl에 정의한 메서드를 쓰기위해 구체타입으로!
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class); // 방금 OrderServiceImpl에 정의한 메서드를 쓰기위해 구체타입으로!
        MemberRepository memberRepository = ac.getBean(MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();
        System.out.println("memberService -> memberRepository1 = " + memberRepository1);
        System.out.println("orderService -> memberRepository2 = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);
        // 같은 인스턴스로 조회
        Assertions.assertThat(memberRepository).isSameAs(memberRepository1);
        Assertions.assertThat(memberRepository1).isSameAs(memberRepository2);

    }
}
