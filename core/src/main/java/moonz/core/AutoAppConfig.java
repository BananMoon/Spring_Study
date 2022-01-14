package moonz.core;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Configuration  // 중복된 객체를 생성하지 않고 등록한 빈을 주입하도록 함
@ComponentScan(
        basePackages = "moonz.core",    // 만약 moonz.core.member로 하면 member 아래부터 컴포넌트  탐색됨
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // 수동으로 등록하는 AppConfig 파일을 제외 !
)
public class AutoAppConfig {
    private final MemberRepository memberRepository;
    private final DiscountPolicy rateDiscountPolicy;

    // 수동 빈 등록이 우선권을 가진다. (자동 빈을 오버라이딩) =>
    // 이제는 중복 빈 등록 시 에러 발생시킴
    // : Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true
/* 수동 빈 등록
    @Bean
    OrderService orderService() {
        return new OrderServiceImpl(memberRepository, discountPolicy);
    }

    @Bean(name="memoryMemberRepository")
    MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
*/
}
