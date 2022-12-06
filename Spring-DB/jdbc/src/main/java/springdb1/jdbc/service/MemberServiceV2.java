package springdb1.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터로 커넥션 전달, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepositoryV2;

    /**
     * 계좌 이체 v2
     * 문제
     * 1. 비즈니스 로직보다 트랜잭션 처리하는 로직이 더 많고 복잡해진다.
     *   커넥션을 생성해서, 매 레파지토리마다 전달해야하고~ 성공하면 커밋하고~ 문제 발생하면 롤백하고~ 종료 처리해주고~
     * 2. 모든 레파지토리 메서드에 커넥션을 전달하는 인자가 추가되어야 한다.
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("같은 커넥션 사용해서 계좌이체 시작 = {}", con);
        try {
            /* 트랜잭션 시작과 같은 의미! */
            con.setAutoCommit(false);
            /* 비즈니스 로직 수행 start! */
            bizLogic(con, fromId, toId, money);

            /* 성공 시 커밋 */
            con.commit();
        } catch (Exception e) {
            con.rollback(); // 실패 시
            throw new IllegalStateException(e);
        } finally {
            /* 트랜잭션 종료 처리 */
            release(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV2.findById(con, fromId);
        Member toMember = memberRepositoryV2.findById(con, toId);
        memberRepositoryV2.update(con, fromId, fromMember.getMoney() - money);

        validation(toMember);  /* 검증: 수신자가 "ex"일 경우 예외 상황 발생 */
        memberRepositoryV2.update(con, toId, toMember.getMoney() + money);
    }

    private static void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);        /* 커넥션 풀 사용할 경우, 살아있는 커넥션을 반납하기 때문에 이를 고려하여 다시 자동커밋 모드로 변경 */
                con.close();        /* 풀에 돌아감. */
            } catch (Exception e) {
                log.info("error: ", e);

            }
        }
    }

    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }

}
