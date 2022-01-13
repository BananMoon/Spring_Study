package moonz.core.order;

import moonz.core.discount.FixDiscountPolicy;
import moonz.core.member.Grade;
import moonz.core.member.Member;
import moonz.core.member.MemoryMemberRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

// DB나 프레임워크 없이 한 기능만을 순수 자바로 테스트하는 단위 테스트
class OrderServiceImplTest {
    @Test
    void createOrder() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));
        OrderServiceImpl orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());    // Mock라이브러리 이용해서 가짜 객체를 만들어도 됨
        Order order = orderService.createOrder(1L, "itemA", 10000);

        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}