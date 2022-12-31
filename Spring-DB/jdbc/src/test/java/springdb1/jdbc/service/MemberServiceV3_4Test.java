package springdb1.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV3;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - DataSource, TransactionManager 자동 등록
 * - 스프링 컨테이너에 DataSource를 빈으로 자동 등록 시, application.properties를 참고하여 등록한다.
  */
@Slf4j
@SpringBootTest
class MemberServiceV3_4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";
    @Autowired
    private MemberRepositoryV3 memberRepositoryV3;
    @Autowired
    private MemberServiceV3_3 memberServiceV3_3;

    @TestConfiguration
    static class testConfig {
        private final DataSource dataSource;

        testConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepositoryV3 memberRepositoryV3() {
            return new MemberRepositoryV3(dataSource);
        }
        @Bean
        MemberServiceV3_3 memberServiceV3_3() {
            return new MemberServiceV3_3(memberRepositoryV3());
        }

    }

    @AfterEach
    void after() throws SQLException {
        memberRepositoryV3.delete(MEMBER_A);
        memberRepositoryV3.delete(MEMBER_B);
        memberRepositoryV3.delete(MEMBER_EX);
    }

    @Test
    void AopCheck() {
        log.info("memberServiceV3_3 class = {}", memberServiceV3_3.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberServiceV3_3)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepositoryV3)).isFalse();   /* 레파지토리에는 AOP를 적용하지 않았으므로 프록시가 아니다. */
    }
    /**
     * 계좌이체 비즈니스 로직에서 동일 커넥션을 사용하면서 9개 사용하던 커넥션 -> 7개로 줄어든 것 확인!
     */
    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);

        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberB);

        // when
        memberServiceV3_3.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member findMemberA = memberRepositoryV3.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV3.findById(memberB.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생- 롤백 체크")
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);

        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberEx);

        // when
        assertThatThrownBy(() -> memberServiceV3_3.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        // then: memberA의 돈만 빠져나가고 memberEx의 돈은 들어오지 않은 상태로 끝나는 로직 시뮬레이션
        Member findMemberA = memberRepositoryV3.findById(memberA.getMemberId());
        Member findMemberEx = memberRepositoryV3.findById(memberEx.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(10000);     /* 롤백! */
        Assertions.assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}