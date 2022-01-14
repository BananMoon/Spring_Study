package moonz.core;

import moonz.core.discount.DiscountPolicy;
import moonz.core.member.MemberRepository;
import moonz.core.member.MemberService;
import moonz.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class AutoAppConfigTest {
    @Test
    void basicScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);

        OrderServiceImpl bean = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = bean.getMemberRepository();
//        DiscountPolicy disCountPolicy = bean.getRateDiscountPolicy();
        System.out.println("memberRepository = " + memberRepository);   // memberRepository = moonz.core.member.MemoryMemberRepository@47605f2f
//        System.out.println("discount = " + disCountPolicy);          // discount = moonz.core.discount.RateDiscountPolicy@2ece4966
    }

}