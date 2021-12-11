package moonz.core.order;

import moonz.core.AppConfig;
import moonz.core.member.Grade;
import moonz.core.member.Member;
import moonz.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {
    //   수정
    MemberService memberService;
    OrderService orderService;
    @BeforeEach
    void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder() {
        // 회원 생성
        Long memberId = 1L;    // primitive 타입으로 하게되면 null을 넣지 못함.
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        // 주문 생성
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}
// 멤버 서비스르