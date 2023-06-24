package moonz.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import moonz.springtx.propagation.log.LogRepository;
import moonz.springtx.propagation.member.MemberRepository;
import moonz.springtx.propagation.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;

// commit(), rollback(), double_commit(), double_commit_rollback()은 복습!
// 여기에 트랜잭션이 1개 더 추가되어 2개의 트랜잭션이 실행되는 경우
// 트랜잭션이 이미 수행 중인데, 여기에 추가로 트랜잭션을 수행하는 경우?
@Slf4j
@SpringBootTest
public class BasicTxTest {
    @Autowired PlatformTransactionManager txManager;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;
    @TestConfiguration
    static class Config {
        @Bean
        public PlatformTransactionManager transactionManager (DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 커밋 시작");
        txManager.commit(txStatus);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback() {
        log.info("트랜잭션 시작");
        TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 롤백 시작");
        txManager.rollback(txStatus);
        log.info("트랜잭션 롤백 완료");
    }
    @Test
    void double_commit() {
        log.info("트랜잭션1 시작");
        TransactionStatus txStatus1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋 시작");
        txManager.commit(txStatus1);
        log.info("트랜잭션1 커밋 완료");

        log.info("트랜잭션2 시작");
        TransactionStatus txStatus2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋 시작");
        txManager.commit(txStatus2);
        log.info("트랜잭션2 커밋 완료");
    }

    // tx1이 커밋 종료된 후 tx2가 생성된 것이므로 tx2의 롤백은 tx1에 영향을 미치지 않는다.
    @Test
    void double_commit_rollback() {
        log.info("트랜잭션1 시작");
        TransactionStatus txStatus1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        txManager.commit(txStatus1);

        log.info("트랜잭션2 시작");
        TransactionStatus txStatus2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 롤백");
        txManager.rollback(txStatus2);
    }

    @DisplayName("외부 트랜잭션의 내부 트랜잭션이 롤백되었을 때 외부 트랜잭션도 롤백된다.")
    @Test
    void inner_rollback() {
        log.info("트랜잭션1 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션2 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 롤백");
        txManager.rollback(inner);  // mark rollback-only

        log.info("트랜잭션1 커밋");
        assertThatThrownBy(() -> txManager.commit(outer))    // rollback-only 마크 있는지 체크 - 있으면 롤백하고 throw UnexpectedRollbackException
                .isInstanceOf(UnexpectedRollbackException.class);
    }

    @DisplayName("REQUIRES_NEW 전파 옵션으로 인해 두개의 외부 트랜잭션을 생성하기 때문에 1개가 롤백되어도 1개는 커밋된다.")
    @Test
    void inner_rollback_requires_new() {
        log.info("트랜잭션1 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction : {}", outer.isNewTransaction());

        log.info("전파 속성을 달리한 후 트랜잭션2 시작");
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = txManager.getTransaction(transactionAttribute);   // Suspending current transaction, creating new transaction
        log.info("inner.isNewTransaction : {}", inner.isNewTransaction());

        log.info("트랜잭션2 롤백");
        txManager.rollback(inner);  // mark rollback-only하지만 초기 트랜잭션이므로 롤백 완료

        log.info("트랜잭션1 커밋");
        txManager.commit(outer);    // outer 트랜잭션만 커밋 완료
    }
}
