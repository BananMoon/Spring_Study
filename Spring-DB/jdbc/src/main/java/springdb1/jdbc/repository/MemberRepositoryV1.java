package springdb1.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import springdb1.jdbc.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource 사용(DI), JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {
    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
     * JdbcUtils 편의 메서드를 이용
     */
    private void close (Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
        JdbcUtils.closeResultSet(rs);
    }
    private Connection getConnection() throws SQLException{
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
