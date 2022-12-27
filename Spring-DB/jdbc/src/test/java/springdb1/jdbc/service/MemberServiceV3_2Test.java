package springdb1.jdbc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV3;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springdb1.jdbc.connection.ConnectionConst.*;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 * 해당 테스트에서는 JDBC 기술을 사용-> JDBC용 트랜잭션 매니저(DataSourceTransactionManager)를 선택해서 주입한다.
  */
class MemberServiceV3_2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 memberRepositoryV3;    /* 추상화된 DataSource 사용 */
    private MemberServiceV3_2 memberServiceV3_2;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV3 = new MemberRepositoryV3(dataSource);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);   /* JDBC용 트랜잭션 매니저 주입. 이때 데이터 소스를 통해 커넥션을 생성한다. */
        memberServiceV3_2 = new MemberServiceV3_2(transactionManager, memberRepositoryV3);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepositoryV3.delete(MEMBER_A);
        memberRepositoryV3.delete(MEMBER_B);
        memberRepositoryV3.delete(MEMBER_EX);
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
        memberServiceV3_2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

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
        assertThatThrownBy(() -> memberServiceV3_2.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        // then: memberA의 돈만 빠져나가고 memberEx의 돈은 들어오지 않은 상태로 끝나는 로직 시뮬레이션
        Member findMemberA = memberRepositoryV3.findById(memberA.getMemberId());
        Member findMemberEx = memberRepositoryV3.findById(memberEx.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(10000);     /* 롤백! */
        Assertions.assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}