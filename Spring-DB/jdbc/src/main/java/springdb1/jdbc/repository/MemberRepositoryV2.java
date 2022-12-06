package springdb1.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import springdb1.jdbc.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * V2. Connection을 파라미터로 넘겨서 동일 커넥션을 사용하도록 하는 레파지토리
 * - 커넥션을 따로 가져오지 않고 파라미터로 받은 커넥션을 사용한다.
 * - 커넥션을 사용한 후에 레파지토리 메서드에서 닫지 않고 (close() X), 이후 서비스 로직이 끝날 때 트랜잭션이 종료되어야 한다.
 */
@Slf4j
public class MemberRepositoryV2  {

    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource) {
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

    /**
     * V2 - 파라미터로 커넥션 전달받아 사용
     */
    public Member findById(Connection con, String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";
        /* finally 구문에서도 사용하기 위해 미리 선언 */
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
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
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    /**
     * V2 - 파라미터로 커넥션 전달받아 사용
     */
    public void update (Connection con, String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete (String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeResultSet(rs);
            /* Connection 종료는 서비스 로직에서 하도록 해야한다. */
        }
    }

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
