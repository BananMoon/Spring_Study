package springdb1.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 * 프록시 기술 이용하여 Spring에서 제공하는 트랜잭션 AOP 애노테이션을 사용한다.
 * 트랜잭션 매니저를 직접 사용하지 않고 애노테이션만 추가하면, 프록시가 동작해서 트랜잭션 시작 - 해당 메서드 호출 - 트랜잭션 커밋/롤백
 */
@Slf4j
public class MemberServiceV3_3 {
//    private final TransactionTemplate txTemplate;   /* 트랜잭션 매니저를 갖고있는 트랜잭션 템플릿 클래스 */
    private final MemberRepositoryV3 memberRepositoryV3;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepositoryV3) {
        this.memberRepositoryV3 = memberRepositoryV3;
    }

    /**
     * 계좌 이체
     * - 애노테이션만으로 해당 메서드 시작 시 트랜잭션 시작하고 메서드가 끝나면 트랜잭션 커밋/롤백 수행
     */
    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        try {
            bizLogic(fromId, toId, money);
        } catch (SQLException e) {
            throw new IllegalStateException(e); /* checked -> unchecked 예외로 전환(bc. 람다에서는 checked 예외는 던질 수 없다. */
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV3.findById(fromId);
        Member toMember = memberRepositoryV3.findById(toId);
        memberRepositoryV3.update(fromId, fromMember.getMoney() - money);

        validation(toMember);
        memberRepositoryV3.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }

}
