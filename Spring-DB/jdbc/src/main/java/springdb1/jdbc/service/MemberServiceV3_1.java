package springdb1.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 * V2. DataSource를 직접 사용(JDBC 이용)하는 문제 => V3.트랜잭션 매니저 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
//    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;    // Di, 현재는 jdbc 기술 사용하므로 DatasourceTransactionManager 주입된다.
    private final MemberRepositoryV3 memberRepositoryV3;

    /**
     * 계좌 이체 v3
     * 트랜잭션 매니저가 커밋/롤백될 시 자동으로 해당 커넥션을 종료(릴리즈) 처리해준다. (더이상 커넥션 쓸일 없으므로)
     */
    public void accountTransfer(String fromId, String toId, int money) {
        /* 트랜잭션 시작 */
        // 트랜잭션의 상태 정보가 들어있으며 트랜잭션 커밋/롤백 시 필요한 객체
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());    //추후 트랜잭션 속성 다룰 예정
        try {
            /* 비즈니스 로직 수행 start! */
            bizLogic(fromId, toId, money);

            /* 성공 시 커밋 */
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status); // 실패 시
            throw new IllegalStateException(e);
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
