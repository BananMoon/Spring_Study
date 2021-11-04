package com.moonz.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob    //대용량 데이터
    private String content; //섬머노트 라이브러리 사용예정 -> <html> 태그가 섞이기 때문에 대용량

    @ColumnDefault("0") // 0으로 초기화
    private int  count; // 조회수

    //Many: board, One: User
    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;    // 자바는 오브젝트를 저장할 수 있지만, DB는 오브젝트를 저장할 수 없다.
    // 자바에서 DB에 맞춰서 데이터를 만들어야함.
    // 자바에서 Object로 생성 후, 자동으로 FK가 생성되도록 @ManyToOne 을 추가

    @OneToMany (mappedBy = "board")     // 하나의 게시물에 여러개의 댓글들
    private List<Reply> reply; // 난 연관관계의 주인이 아니다.(FK가 아니다)  DB에 칼럼을 만들지 마세요.
    // 나는 단지 JOIN문을  통해 값을 얻기위해 필요한 것
    // mappedBy= "연관관계맺는 필드 이름"

    @CreationTimestamp
    private Timestamp createdAt;
}
