package moonz.core.discount;

import moonz.core.annotation.MainDiscountPolicy;
import moonz.core.member.Grade;
import moonz.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@MainDiscountPolicy //@Qualifier("mainDiscountPolicy") : 컴파일 시 체크 x
public class RateDiscountPolicy implements DiscountPolicy {
    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {     // VIP이면 가격의 10% 할인
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
