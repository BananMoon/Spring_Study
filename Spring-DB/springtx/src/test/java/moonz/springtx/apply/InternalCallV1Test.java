package moonz.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {
    @Autowired CallService callService;

    @Test
    @DisplayName("proxy 적용 여부 체크")
    void printProxy() {
        log.info("callService class = {}", callService.getClass()); /* InternalCallV1Test$CallService$$EnhancerBySpringCGLIB$$8a64e448 */
    }

    @Test
    @DisplayName("외부에서 클래스의 메서드 호출-트랜잭션 적용")
    void internalCall() {
        callService.internal();
    }

    /**
     * <프록시 방식의 AOP 한계>
     * 외부에서 특정 클래스의 메서드 호출 시, 해당 메서드가 트랜잭션이 적용이 안되어있다면 프록시가 아닌 실제 인스턴스의 메서드를 호출한다.
     * 그 메서드 내부에서 동일 인스턴스에 있는 트랜잭션이 적용된 메서드를 호출하면?
     * this.메서드() 를 호출하므로 마찬가지로 프록시가 적용되지 않은 메서드가 호출되기 때문에 트랜잭션이 적용되지 않는다.
     */
    @Test
    @DisplayName("클래스의 내부 메서드에서 동일 내부 메서드 호출-트랜잭션 미적용")
    void externalCall() {
        callService.external();
    }
    @TestConfiguration
    static class InternalCallV1TestConfig {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }
    @Slf4j
    static class CallService {
        public void external() {
            log.info("call external");
            printTxInfo();
            internal(); // 내부에서 트랜잭션 메서드 호출
        }

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }
        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
            boolean isReadOnlyTx = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", isReadOnlyTx);
        }
    }

}
