package moonz.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

/**
 * 스프링 트랜잭션(AOP)을 사용하고 있는지 확인하는 테스트
 * - 주입된 프록시 클래스는 실제 클래스를 상속하는 관계이다.
 * - 선언적 트랜잭션 붙은 경우 순서
 * 1. 클래스가 참조 클래스의 tx() 메서드 호출 시 프록시의 tx()가 호출된다.
 * 2. 이때 tx() 메서드는 트랜잭션을 사용할 수 있으므로 트랜잭션을 시작한 다음 실제 클래스의 tx() 메서드를 호출한다.
 * 3. tx() 메서드 실행이 끝난 뒤에 프록시는 트랜잭션 로직을 커밋/롤백하여 트랜잭션을 종료한다.
 */
@Slf4j
@SpringBootTest
public class TxUsageBasicTest {
    @Autowired BasicService basicService;

    @Test
    @DisplayName("선언적 트랜잭션 방식으로 인한 AOP 기반 트랜잭션 적용 여부 확인.")
    void proxyCheck() {
        /* TxBasicTest$BasicService$$EnhancerBySpringCGLIB$$e47ad2e4 : 트랜잭션을 적용하는 프록시 */
        log.info("aop class = {}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    /**
     * tx() 메서드 실행 시 로그
     TransactionInterceptor : Getting transaction for [moonz.springtx.apply.TxBasicTest$BasicService.tx] => 트랜잭션 시작
     TxBasicTest$BasicService : call tx
     TxBasicTest$BasicService : tx active? = true
     TransactionInterceptor : Completing transaction for [moonz.springtx.apply.TxBasicTest$BasicService.tx] => 트랜잭션 완료
     */
    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }
    @Slf4j
    static class BasicService {
        @Transactional  /* 트랜잭션 AOP 적용의 대상이 되는 tx 메서드. 선언적 트랜잭션 방식. AOP 기반 동작 */
        public void tx() {
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();   /* 현재 쓰레드에 트랜잭션이 적용되어 있는가? */
            log.info("tx active? = {}", txActive);
        }

        public void nonTx() {
            log.info("call nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active? = {}", txActive);
        }
    }

}
