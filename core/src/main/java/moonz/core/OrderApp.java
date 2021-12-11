package moonz.core;

import moonz.core.member.Grade;
import moonz.core.member.Member;
import moonz.core.member.MemberService;
import moonz.core.member.MemberServiceImpl;
import moonz.core.order.Order;
import moonz.core.order.OrderService;
import moonz.core.order.OrderServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//TEST (수동)
public class OrderApp {
    public static void main(String[] args) {
        // 수정
//        MemberService memberService;
//        OrderService orderService;
//
//        AppConfig appConfig = new AppConfig();
//        memberService = appConfig.memberService();
//        orderService = appConfig.orderService();

        // 스프링 : 스프링컨테이너 생성
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // 스프링 : 스프링 컨테이너에서 객체를 찾아서 꺼내온다. (등록된 메서드명, 꺼낼 클래스 타입)
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        OrderService orderService = ac.getBean("orderService", OrderService.class);
        // 멤버 생성
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        // 주문 생성
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = " + order);
    }
}
