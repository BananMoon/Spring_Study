package moonz.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

// 가짜의 네트워크 연결 객체
public class NetworkClient  {
    private String url; // 접속할 서버 url

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect : " + url);
    }

    public void call(String message) {
        System.out.println("call : " + url + " , message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close " + url);
    }

    //의존관계 주입(PropertiesSet)이 끝나면 호출해주는 메서드
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

   // 빈이 종료될 때 호출
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
