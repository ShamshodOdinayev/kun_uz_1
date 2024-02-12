package com.example.entity;

import com.example.enums.ArticleLikeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article_like")
public class ArticleLikeEntity extends BaseEntity {
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "article_id")
    private String articleId;
    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private ArticleEntity article;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ArticleLikeStatus status;

}
