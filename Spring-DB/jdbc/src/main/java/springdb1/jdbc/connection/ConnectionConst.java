package springdb1.jdbc.connection;

/**
 * 객체화하지못하도록 abstract 사용...
 */
public abstract class ConnectionConst {
    public static final String URL = "jdbc:h2:tcp://localhost/~/h2/spring-db-1"; /* h2 데이터베이스 접근 */
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";

}
