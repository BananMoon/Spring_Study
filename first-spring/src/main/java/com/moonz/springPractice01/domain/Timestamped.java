package com.moonz.springPractice01.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // 이 클래스를 상속했을 때, 멤버변수들을 컬럼으로 인식하게 합니다.
@EntityListeners(AuditingEntityListener.class) // Entity를 보다가 생성/수정 시간을 자동으로 반영하도록 설정
public abstract class Timestamped { // abstract? 상속으로만 사용가능한 클래스 (new 불가)

    //LocalDateTime : 시간을 나타내는 자료형
    @CreatedDate // 생성일자임을 spring에게 알려주는 annotation
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 수정일자
    private LocalDateTime modifiedAt;
}