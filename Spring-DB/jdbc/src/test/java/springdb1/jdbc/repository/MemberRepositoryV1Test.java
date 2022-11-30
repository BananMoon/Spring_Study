package springdb1.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import springdb1.jdbc.domain.Member;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springdb1.jdbc.connection.ConnectionConst.*;

@Slf4j
class MemberRepositoryV1Test {
    MemberRepositoryV1 repositoryV1;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager - 항상 새 커넥션 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀링 (설정을 위해 구현체로 생성)
        // 커넥션 사용 및 반환 => conn0만 사용하게 됨.
        // Hikari Proxy 객체를 생성해서, TCP/IP 연결된 커넥션과 연결시키는 것이므로 Hikari Proxy 객체는 모두 다름.
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    /**
     * MemberRepositoryV0을 이용해서 쿼리를 수행할 때마다 커넥션이 새로 생성되서 수행된다. 총 6번의 커넥션이 생성되는 것을 확인.
     */
    @Test
    void crud() throws SQLException {
        /* save */
        Member member = new Member("memberV4", 10000);
        repositoryV1.save(member);

        /* findById */
        Member findMember = repositoryV1.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        log.info("findMember == member : {} ", findMember == member);   /* 참조값 비교 */
        assertThat(findMember).isEqualTo(member);    /* 참조값 비교가 아닌 객체 필드 값 비교 */

        /* update */
        repositoryV1.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryV1.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        /* delete */
        repositoryV1.delete(member.getMemberId());
        assertThatThrownBy(() -> repositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}