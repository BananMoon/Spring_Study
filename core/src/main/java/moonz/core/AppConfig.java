package moonz.core;

import moonz.core.discount.RateDiscountPolicy;
import moonz.core.member.MemberService;
import moonz.core.member.MemberServiceImpl;
import moonz.core.member.MemoryMemberRepository;
import moonz.core.order.OrderService;
import moonz.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 팩터리 메서드를 통해서 우회하여 등록하는 방법
@Configuration
public class AppConfig {
     // 스프링 컨테이너에 등록! 해당 메서드 이름으로 등록됨.
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(getMemberRepository());
    }
    // orderService에서는 레포지토리와 할인 정책을 사용하므로 생성자에서 2개 모두 주입.
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(getMemberRepository(), getDiscountPolicy());
    }
    @Bean
    public MemoryMemberRepository getMemberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public RateDiscountPolicy getDiscountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
