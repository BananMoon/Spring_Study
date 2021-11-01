package com.moonz.bookspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSpringbootApplication.class, args);
    }
// SpringApplication.run() : 내장 WAS(Web Application Server)를 실행
    // 내장 WAS : 별도로 외부에 서버(톰캣)를 설치할 필요 없고, 스프링 부트로 만들어진 Jar파일(실행가능한 Java 패키징 파일)로 실행.
    // 내장 WAS를 권장하는 이유 : 언제 어디서나 같은 환경에서 스프링부트를 배포할 수 있기 때문.
    // 새로운 서버가 추가되면 모든 서버가 같은 was 환경을 구축해야하기 때문. 버전 변경 시 시간이 오래 걸림.
}
