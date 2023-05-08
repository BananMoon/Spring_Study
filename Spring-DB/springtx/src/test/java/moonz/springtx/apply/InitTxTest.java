package moonz.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

/**
 * 초기화 메서드는 트랜잭션이 적용되지 않는다.
 */
@SpringBootTest // 스프링 부트가 동작해야함
public class InitTxTest {
    @Autowired Temp temp;

    @Test
    void unitTxActiveTest() {
        // temp 인스턴스 생성(초기화) 시에 @PostConstrct에 의해 동작
    }

    @TestConfiguration
    static class InitTxTestConfig { /* Config 클래스는 static 이어야 한다. */
        @Bean
        Temp temp() {
            return new Temp();
        }
    }

    @Slf4j
    public static class Temp {
        /* 스프링이 public에만 트랜잭션 적용 */
        @PostConstruct
        @Transactional
        void initV1() {
            log.info("PostConstruct tx active = {}", TransactionSynchronizationManager.isActualTransactionActive());
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        void initV2() {
            log.info("ApplicationReadyEvent tx active = {}", TransactionSynchronizationManager.isActualTransactionActive());
        }
    }
}
