package moonz.core;

import moonz.core.member.Grade;
import moonz.core.member.Member;
import moonz.core.member.MemberService;
import moonz.core.member.MemberServiceImpl;
import moonz.core.order.Order;
import moonz.core.order.OrderService;
import moonz.core.order.OrderServiceImpl;
//TEST (수동)
public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        // 멤버 생성
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        // 주문 생성
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = " + order);
    }
}
