package moonz.core.order;

import moonz.core.discount.DiscountPolicy;
import moonz.core.discount.FixDiscountPolicy;
import moonz.core.member.Member;
import moonz.core.member.MemberRepository;
import moonz.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId);
//        System.out.println("member name: " +member.getName());

        // 2.  할인 적용-> 단일 체계 원칙을 잘 지킴. 할인 관련 변경은 할인 쪽에서만 변경하면 됨.
        int discountPrice = discountPolicy.discount(member, itemPrice);  // 확장성을 고려해서 member 자체를 넘길지 grade만 넘길지 정하면 됨.
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}