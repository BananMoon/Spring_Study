package com.moonz.securitypractice.common.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor
public abstract class BaseTimeEntity {
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(name="last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;
}
