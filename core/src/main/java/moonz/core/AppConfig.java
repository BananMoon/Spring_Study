package moonz.core;

import moonz.core.discount.FixDiscountPolicy;
import moonz.core.member.MemberService;
import moonz.core.member.MemberServiceImpl;
import moonz.core.member.MemoryMemberRepository;
import moonz.core.order.OrderService;
import moonz.core.order.OrderServiceImpl;

public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }
    // orderService에서는 레포지토리와 할인 정책을 사용하므로 생성자에서 2개 모두 주입.
    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
