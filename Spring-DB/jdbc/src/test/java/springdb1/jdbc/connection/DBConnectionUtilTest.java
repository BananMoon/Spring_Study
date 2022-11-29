package springdb1.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static springdb1.jdbc.connection.ConnectionConst.*;

@Slf4j
class DBConnectionUtilTest {

    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();  // assertj 사용 권고. why??
    }

    /** 구현체 : DriverManagerDataSource
     DBConnectionUtil.getConnection()을 통해 커넥션 획득하는 경우, 커넥션 획득마다 연결을 위한 정보를 넘겨야했다.
     아래 테스트는 DataSource 객체를 생성 시에 1번 넘기고, 그 후 커넥션을 획득할 때는 (즉, DataSource를 사용하는 시점에는) 정보 없이 호출하면 된다.
     DriverManagerDataSource 또한 새 커넥션 획득하는 방법
     */
    @Test
    void dataSourceDriverManager() throws SQLException {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD); /*
생성 시점 */
        useDataSource(dataSource); /* 사용 시점 */
    }

    /** 구현체 : 커넥션 풀
     JDBC 라이브러리 사용하면 자동으로 HikariCP가 import된다.
     알아둘 점!
     1. 커넥션을 생성해서 커넥션 풀에 채워주는 작업은 상대적으로 오래 걸리는 일이기 때문에 애플리케이션 실행에 영향미치지 않도록 별도의 스레드가 실행된다.
     2. 커넥션 풀에 대기 커넥션보다 그 이상으로 커넥션을 생성하려고 하면 지정된 대기 시간만큼 대기하다가 커넥션이 생기면 가져온다.
     */
    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");
        useDataSource(dataSource);  /* 커넥션 1개 획득했으므로-> After adding stats (total=10, active=1, idle=9, waiting=0) */
        Thread.sleep(1000);  // 약 1초. 기다리지 않으면 10개 커넥션 풀 생성 로그가 다 찍히지 않고 종료됨..
    }

    /** 설정과 사용의 분리
     - 장점: 객체를 설정하는 부분과 사용하는 부분을 명확하게 분리할 수 있다. 사용하는 곳에서는 설정 속성들에 의존하지 않을 수 있게 된다.
     + 상황) 10개의 커넥션이 모두 active일 경우,
     대기하다가 : MyPool - Pool stats (total=10, active=10, idle=0, waiting=1)
     에러: MyPool - Connection is not available, request timed out after 30006ms.
     */
    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con = dataSource.getConnection();
        /*
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        Connection con3 = dataSource.getConnection();
        Connection con4 = dataSource.getConnection();
        Connection con5 = dataSource.getConnection();
        Connection con6 = dataSource.getConnection();
        Connection con7 = dataSource.getConnection();
        Connection con8 = dataSource.getConnection();
        Connection con9 = dataSource.getConnection();
        Connection con10 = dataSource.getConnection();
        */
        log.info("connection= {}, class= {}", con, con.getClass());
    }

}


