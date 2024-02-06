package com.example.entity;

import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "shared_count")
    private Integer sharedCount;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "visible")
    private Boolean visible = true;
    @Column(name = "view_count")
    private Integer viewCount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status = ArticleStatus.NOT_PUBLISHED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_attach_id")
    private ArticleAttachEntity articleAttach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_news_type_id")
    private ArticleNewsTypeEntity articleNewsType;

    @Column(name = "region_id")
    private Integer regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;


    @Column(name = "category_id")
    private Integer categoryId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;


    @Column(name = "moderator_id")
    private Integer moderatorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;

    @Column(name = "publisher_id")
    private Integer publisherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private ProfileEntity publisher;

    @Column(name = "photo_id")
    private String photoId;
    @ManyToOne
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @OneToMany(mappedBy = "article")
    private List<ArticleNewsTypeEntity> articleTypesList;

}
