package com.moonz.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

//ORM -> Java Object -> (MySQL) 테이블로 매핑해주는 기술
@Builder
@Data   // Setter, Getter O
@NoArgsConstructor
@AllArgsConstructor
@Entity //User 클래스를 통해 MySQL에 테이블이 생성됨
//@DynamicInsert  insert 시에 null인 필드를 제외시켜준다.
public class User {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private Long id;
    // GenerationType.TABLE : table에 번호를 설정하고, 그 번호를 따라가겠다.
    // Generation.SEQUENCE : auto-increment 사용 불가
    // Generation.IDENTITY : DB의 넘버링전략 (Oracle: Sequence, mysql: auto_increment) 따라가겠다.

    @Column(nullable = false, length = 30, unique = true)
    private String username;    // 아이디

    @Column(nullable = false, length = 100) // 123456-> 해쉬(비번 암호화)된 값을 db에 저장하기 때문
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    //@ColumnDefault("'user'")    // 'user' 로 초기화
    // String으로 하면 오타가 그대로 들어가므로 USER, ADMIN으로 제한둘 수 있도록 한다.
    // DB는 RoleType이 없으므로 String으로 알려줘야한다.
    @Enumerated(EnumType.STRING)
    private RoleType role;    // Enum을 쓰는게 좋다.-> Data의 도메인을 만들 수 있기 때문.
    // role은 admin, user, manager 권한을 줘서, 각 역할에 권한을 다르게 줄 수 있음.
    // String이면 managerrrr와같은 것이 가능하게 되지만, Enum으로 설정 시, 도메인을 세가지로 설정해놀 수 있음
    // 도메인? '범위'를 뜻함. ex) 고등학생 : 1-3

    @CreationTimestamp  //시간이 자동으로 입력
    private Timestamp createdAt;
}
