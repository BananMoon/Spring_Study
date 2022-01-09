package moonz.core;

import moonz.core.discount.DiscountPolicy;
import moonz.core.member.MemberRepository;
import moonz.core.member.MemoryMemberRepository;
import moonz.core.order.OrderService;
import moonz.core.order.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "moonz.core",    // 만약 moonz.core.member로 하면 member 아래부터 컴포넌트  탐색됨
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // 수동으로 등록하는 AppConfig 파일을 제외 !
)
public class AutoAppConfig {
    MemberRepository memberRepository;
    DiscountPolicy discountPolicy;

    @Autowired
    public AutoAppConfig(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Bean
    OrderService orderService() {
        return new OrderServiceImpl(memberRepository, discountPolicy);
    }

    @Bean(name="memoryMemberRepository")
    MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
