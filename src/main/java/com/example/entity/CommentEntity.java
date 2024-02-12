package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "profile_id")
    private Integer profileId;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "reply_id")
    private Integer replyId;
    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private ArticleEntity article;
    @Column(name = "article_id")
    private String articleId;
}
