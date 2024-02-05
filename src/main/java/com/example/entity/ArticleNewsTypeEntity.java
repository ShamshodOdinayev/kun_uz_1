package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "article_news_type")
public class ArticleNewsTypeEntity extends BaseEntity {
    @Column(name = "article_id")
    private String articleId;
    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private ArticleEntity article;
    @Column(name = "types_id")
    private Integer typesId;
    @ManyToOne
    @JoinColumn(name = "types_id", insertable = false, updatable = false)
    private ArticleTypeEntity types;
}
