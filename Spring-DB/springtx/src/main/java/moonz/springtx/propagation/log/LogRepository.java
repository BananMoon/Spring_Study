package moonz.springtx.propagation.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

// 트랜잭션 시작 위치 유동적으로 변경하고자 Jpa -> EntityManager 직접 이용 방식으로 수정

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final EntityManager em;

    /* <주의>
     REQUIRES_NEW 를 사용하면 하나의 HTTP 요청에 동시에 2개의 데이터베이스 커넥션을 사용하게 된다.
     따라서 성능이 중요한 곳에서는 이런 부분을 주의해서 사용해야 한다.
     REQUIRES_NEW 를 사용하지 않고 문제를 해결할 수 있는 단순한 방법이 있다면, 그 방법을 선택하는 것이 더 좋다.
     - 참고: MemberService와 LogRepository를 호출하는 MemberFacade 클래스를 앞단에 둔다. 여기에 트랜잭션을 걸지 않으면
     MemberService와 LogRepository부터 트랜잭션이 생성되므로 'REQUIRES' 속성만으로도 두 트랜잭션을 분리할 수 있게된다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Log logMessage) {
        log.info("log 저장"); em.persist(logMessage);
        if (logMessage.getMessage().contains("로그예외")) { log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        } }

    public Optional<Log> find(String message) {
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList().stream().findAny();
    }
}