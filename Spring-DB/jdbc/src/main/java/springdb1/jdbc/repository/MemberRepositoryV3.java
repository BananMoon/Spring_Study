package springdb1.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import springdb1.jdbc.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * V3. 트랜잭션 - 트랜잭션 매니저
 * 트랜잭션 동기화를 사용하기 위해 DataSourceUtils 사용
 * 커넥션 획득 시 : DataSourceUtils.getConnection()
 * 커넥션 닫을 시 : DataSourceUtils.releaseConnection()
 * DataSource를 직접 이용해서 가져오는 방식 -> Utils 이용해서
 */
@Slf4j
public class MemberRepositoryV3 {

    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {
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

    public void update (String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";
        PreparedStatement pstmt = null;
        Connection con = null;

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
            close(con, pstmt, null);
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
        }
    }

    private void close (Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeStatement(stmt);
        /* DataSourceUtils를 사용하여 트랜잭션 동기화 사용 */
//        JdbcUtils.closeConnection(con);
        DataSourceUtils.releaseConnection(con, dataSource);
        JdbcUtils.closeResultSet(rs);
    }
    /* 주어진 dataSource로부터 JDBC Connection을 얻는다.
    * DataSourceUtils.getConnection(~): 트랜잭션 매니저가 관리하는 커넥션을 반환.
    * */
    private Connection getConnection() throws SQLException{
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
