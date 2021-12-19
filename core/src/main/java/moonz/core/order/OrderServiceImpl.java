package moonz.core.order;

import moonz.core.discount.DiscountPolicy;
import moonz.core.member.Member;
import moonz.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    // 오직 추상화에만 의존!
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;  // DIP 지킨 모습! final은 초기화를 해줘야하므로 지워준다.
    @Autowired  // 생성자에서 여러 의존관계도 한번에 주입받을 수 있다.
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
}