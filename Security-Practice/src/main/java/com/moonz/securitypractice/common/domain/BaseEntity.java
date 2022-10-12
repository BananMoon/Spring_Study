package com.moonz.securitypractice.common.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass   // 해당 추상 클래스를 상속할 경우 해당 필드를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)  // 해당 클래스에 Auditing 기능을 포함시킨다.
public abstract class BaseEntity extends BaseTimeEntity{
    @CreatedBy
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;
}
