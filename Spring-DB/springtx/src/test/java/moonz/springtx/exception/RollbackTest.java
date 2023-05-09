package moonz.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예외가 터졌는데 왜 커밋하지? 에 초점!
 * 예외는 비즈니스 예외와 시스템 예외가 있다.
 * 비즈니스 예외는 데이터가 커밋되어야하는 경우가 있다.
 */
@SpringBootTest
public class RollbackTest {
    @Autowired
    RollbackService service;
    // 출력 : Rolling back JPA transaction on EntityManager
    @Test
    @DisplayName("unchecked 예외니까 롤백됐는지 확인")
    void uncheckedException() {
        Assertions.assertThatThrownBy(() -> service.uncheckedException())
                .isInstanceOf(RuntimeException.class);
    }
    // 출력 : Committing JPA transaction on EntityManager
    @Test
    @DisplayName("checked 예외니까 커밋됐는지 확인")
    void checkedException() {
        Assertions.assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(MyException.class);
    }

    // 출력 : Rolling back JPA transaction on EntityManager
    @Test
    @DisplayName("rollbackFor 옵션 테스트. checked 예외인데 롤백됐는지 확인")
    void rollbackfor() {
        Assertions.assertThatThrownBy(() -> service.rollbackfor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }
    @Slf4j
    static class RollbackService {
        // unchekced Exception -> 롤백
        @Transactional
        public void uncheckedException() {
            log.info("call RuntimeException(unchecked)");
            throw new RuntimeException();
        }
        // checked Exception -> 커밋
        @Transactional(rollbackFor = MyException.class)
        public void rollbackfor() throws MyException {
            log.info("call rollbackfor");
            throw new MyException();
        }

        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

    }

    static class MyException extends Exception {
    }

}
