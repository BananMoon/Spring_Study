package com.moonz.springPractice01.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

@Getter
@NoArgsConstructor // 기본생성자(Argument가 없는 생성자)를 대신 생성해줍니다.
@Entity // 테이블임을 나타냅니다.
public class Course extends Timestamped {

    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue(strategy = GenerationType.AUTO) // AUTO INCREMENT, 자동 증가 명령
    private Long id;

    @Column(nullable = false) // NOT NULL, 컬럼 값이 반드시 존재해야 함을 나타냅니다.
    private String title;

    @Column(nullable = false)
    private String tutor;

    //repository에서 setter는 해줌
    public Course(String title, String tutor) {
        this.title = title;
        this.tutor = tutor;
    }

    // U(update)
    public void update(CourseRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.tutor = requestDto.getTutor();
    }
}
