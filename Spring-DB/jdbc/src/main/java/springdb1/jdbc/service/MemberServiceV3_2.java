package springdb1.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 * V3에서 확인할 수 있는 트랜잭션 관련 로직은 모든 서비스에서 반복된다. (트랜잭션 시작, 커밋/롤백)
 * 이런 중복 코드를 제거하기 위해 스프링은 트랜잭션 템플릿(TransactionTemplate 클래스)을 사용한다.
 * => 트랜잭션 시작과 커밋/롤백 코드를 제거하고 비즈니스 로직만 구현하면 된다.
 */
@Slf4j
public class MemberServiceV3_2 {
    private final TransactionTemplate txTemplate;   /* 트랜잭션 매니저를 갖고있는 트랜잭션 템플릿 클래스 */
    private final MemberRepositoryV3 memberRepositoryV3;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepositoryV3) {
        this.txTemplate = new TransactionTemplate(transactionManager);   /* 트랜잭션 매니저를 주입받으면 내부에서 트랜잭션 템플릿을 사용함. */
        this.memberRepositoryV3 = memberRepositoryV3;
    }


    /**
     * 계좌 이체 v3
     * - 예외) checked 예외는 커밋, unchecked/런타임 예외는 롤백이 default이다. (물론 변경 가능)
     * - 람다를 사용했기 때문에 checked 예외를 밖으로 던질 수 없어서 unchecked 예외로 전환한 뒤 던지도록 했다.
     */
    public void accountTransfer(String fromId, String toId, int money) {
        /* 인자로 비즈니스 로직 전달 -> 1. 트랜잭션 시작 2. 인자 수행 3. 커밋/롤백 수행 */
        txTemplate.executeWithoutResult((transactionStatus -> {
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e); /* checked -> unchecked 예외로 전환(bc. 람다에서는 checked 예외는 던질 수 없다. */
            }
        }));
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
