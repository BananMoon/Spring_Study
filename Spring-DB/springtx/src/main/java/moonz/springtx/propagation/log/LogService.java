package moonz.springtx.propagation.log;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@AllArgsConstructor
public class LogService {
    public final LogRepository logRepository;

    public Log save(String username) {
        Log logMessage = new Log(username);
        log.info("== LogRepository 호출 시작 ==");
        if (username.contains("로그예외")) {
            throw new RuntimeException("예외 발생");
        }
        Log savedLog = logRepository.save(logMessage);
        log.info("== LogRepository 호출 종료 ==");
        return savedLog;
    }
}
