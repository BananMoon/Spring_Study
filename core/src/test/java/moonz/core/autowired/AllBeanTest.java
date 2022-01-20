package moonz.core.autowired;

import moonz.core.AutoAppConfig;
import moonz.core.discount.DiscountPolicy;
import moonz.core.member.Grade;
import moonz.core.member.Member;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

public class AllBeanTest {

    @Test
    void findAllBean() {
        System.out.println("=========1. =====================");

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        // TDD
        System.out.println("=========2. =====================");
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");   // discount가 얼마나 되는지 확인하는 메서드

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");   // discount가 얼마나 되는지 확인하는 메서드
        assertThat(rateDiscountPrice).isEqualTo(2000);

    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        @Autowired
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
    }
    // DiscountService.class 를 빈 환경으로 등록할 경우, 등록된 bean이 없으므로 아무것도 뜨지 않음.
    // policyMap = {fixDiscountPolicy=moonz.core.discount.FixDiscountPolicy@5b1f29fa, rateDiscountPolicy=moonz.core.discount.RateDiscountPolicy@aeab9a1}
    // policies = [moonz.core.discount.FixDiscountPolicy@5b1f29fa, moonz.core.discount.RateDiscountPolicy@aeab9a1]
}
