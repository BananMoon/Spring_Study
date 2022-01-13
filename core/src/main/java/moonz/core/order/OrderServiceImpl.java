package moonz.core.order;

import lombok.RequiredArgsConstructor;
import moonz.core.discount.DiscountPolicy;
import moonz.core.member.Member;
import moonz.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    // 오직 추상화에만 의존!
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;  // DIP 지킨 모습! final을 설정함으로써 (생성자를 통해) 초기화를 무조건 해줘야한다.(setter 생성할 경우 지워줘야한다.)

    /* 수정자 주입 : setter를 생성해줄 경우 final 키워드 없이 객체를 선언해야한다.
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {    // memberRepository가 스프링 빈으로 등록 안됐을 수도 있음. 선택적으로 주입 가능하다.
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    } */
    @Autowired  // 생성자에서 여러 의존관계를 한번에 주입받을 수 있다.
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId);
//        System.out.println("member name: " +member.getName());

        // 2.  할인 적용-> 단일 체계 원칙을 잘 지킴. 할인 관련 변경은 할인 쪽에서만 변경하면 됨.
        int discountPrice = discountPolicy.discount(member, itemPrice);  // 확장성을 고려해서 member 자체를 넘길지 grade만 넘길지 정하면 됨.
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // test 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
/*    public DiscountPolicy getRateDiscountPolicy() {
        return rateDiscountPolicy;
    }*/
}