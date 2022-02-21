package moonz.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)       // request 스코프로 지정. HTTP 요청 당 하나씩 생성되고, 요청이 끝나는 시점에 소멸된다.
public class MyLogger {
    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "][" + requestURL + "] " + message);
    }
    // 고객 요청이 들어올 때 컨테이너에서 조회 시 최초로 빈이 생성, 의존성 주입 후 init() 호출
    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean init : " + this);
    }
    @PreDestroy
    public void destroy() {
        System.out.println("[" + uuid + "] request scope bean destroy : " + this);
    }
}