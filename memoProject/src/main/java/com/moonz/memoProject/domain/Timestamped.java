package com.moonz.memoProject.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // 상속받은 Entity(Memo 클래스)가 자동으로 칼럼으로 인식!
@EntityListeners(AuditingEntityListener.class)  // 생성/변경 시간을 자동으로 업데이트합니다.
public abstract class Timestamped { //just 빵틀! new 이용해서 객체 생성 X 상속을 해야만 사용할 수 있음.
    //LocalDateTIme : 시간을 나타내는 자료형
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}