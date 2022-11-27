package springdb1.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import springdb1.jdbc.connection.DBConnectionUtil;
import springdb1.jdbc.domain.Member;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 *
 * Statement : static SQL statement를 수행하고 결과를 응답하는데 사용되는 인터페이스
 * PreparedStatement :
 *  - Statement 인터페이스 상속 + 파라미터 바인딩 가능한 클래스 => 더 다양한 기능 제공.
 *  - SQL Injection 공격을 막을 수 있다.
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save (Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = getConnection();  /* 연결 목적 */
        PreparedStatement pstmt = null; /* 쿼리 날릴 애 */

        try {
            pstmt = con.prepareStatement(sql);
            /* 파라미터 바인딩 */
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            pstmt.executeUpdate();  /* 쿼리 실행!해서 db에 전달. 영향을 준 row 갯수를 반환 */
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;    /* 밖으로 예외 던짐 */
        } finally {
            close(con, pstmt, null);
        }
    }

    /**
     * ResultSet :
     * - 결과 조회 시 사용되는 인터페이스
     * - ResultSet 내부에 있는 커서(cursor)를 이동시켜서 다음 데이터를 조회한다.
     * - .next() : 최초의 커서는 데이터를 가리키지 않기 때문에 최초 1번 호출해서 데이터를 향하도록 해야한다. 데이터가 없으면 false 반환
     * - .getString("칼럼명"), .getInt("칼럼명") : 원하는 칼럼과 반환값에 따라 getXXX("")를 호출한다.
     */
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";
        /* finally 구문에서도 사용하기 위해 미리 선언 */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();  /* 데이터 조회 시 executeQuery() 사용 */
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found (memberId = " + memberId + ")");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;    /* Checked Exception */
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update (String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void delete (String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    /**
     * 쿼리 실행 후 리소스를 정리해야 하는데, 항상 역순으로 정리해야 한다.
     * 리소스 정리해주지 않으면 커넥션 부족 장애가 발생할 수 있다.
     */
    private void close (Connection con, Statement stmt, ResultSet rs) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);   /* 닫을 때 발생하는 예외는 따로 처리해줄 방도가 없으므로 로깅만 진행 */
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
