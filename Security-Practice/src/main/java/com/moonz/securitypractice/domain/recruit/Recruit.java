package com.moonz.securitypractice.domain.recruit;


import com.moonz.securitypractice.common.domain.BaseEntity;
import com.moonz.securitypractice.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recruit")
public class Recruit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;
    @Column
    private String title;

    private String content;

    private LocalDate projectStartDate;

    private LocalDate projectEndDate;

    private int recruitMemberCount;

    // 모집타입은 애노테이션으로.

    // 활동 지역도 Enum일듯?
    private String activityArea;

    private String status;

    private int commentCount;

    @Column(name = "bookmark_count", nullable = false)
    private int bookmarkCount;

    @ManyToOne(fetch = FetchType.LAZY)  // 모집글 조회 시 회원 엔티티가 필요한가?
    private Member member;
}
