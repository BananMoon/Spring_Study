package springdb1.jdbc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV2;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springdb1.jdbc.connection.ConnectionConst.*;

/**
 * 기본 동작, 트랜잭션이 없어서 문제 발생
 */
class MemberServiceV2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV2 memberRepositoryV2;    /* 추상화된 DataSource 사용 */
    private MemberServiceV2 memberServiceV2;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV2 = new MemberRepositoryV2(dataSource);
        memberServiceV2 = new MemberServiceV2(dataSource, memberRepositoryV2);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepositoryV2.delete(MEMBER_A);
        memberRepositoryV2.delete(MEMBER_B);
        memberRepositoryV2.delete(MEMBER_EX);
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

        memberRepositoryV2.save(memberA);
        memberRepositoryV2.save(memberB);

        // when
        memberServiceV2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member findMemberA = memberRepositoryV2.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV2.findById(memberB.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생- 롤백 체크")
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);

        memberRepositoryV2.save(memberA);
        memberRepositoryV2.save(memberEx);

        // when
        assertThatThrownBy(() -> memberServiceV2.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        // then: memberA의 돈만 빠져나가고 memberEx의 돈은 들어오지 않은 상태로 끝나는 로직 시뮬레이션
        Member findMemberA = memberRepositoryV2.findById(memberA.getMemberId());
        Member findMemberEx = memberRepositoryV2.findById(memberEx.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(10000);     /* 롤백! */
        Assertions.assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}