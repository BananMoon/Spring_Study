package moonz.springtx.propagation.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Long> {
    @Transactional
    Optional<Log> findByMessage(String message);
    // save() 따로 선언하지 않아도 트랜잭션 생성.
}
