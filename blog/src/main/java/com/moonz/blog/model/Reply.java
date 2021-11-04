package com.moonz.blog.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Reply {
// Id  content  userId  boardId
// 1       좋아요        2             1
// 2     같이해요     3             1
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne // 하나의 게시글에, 여러개의 댓글
    @JoinColumn(name="boardId")
    private Board board;

    @ManyToOne  // 하나의 user가 여러개의 댓글
    private User user;  //답변을 누가 적었는가

    @CreationTimestamp
    private Timestamp createdAt;
}
