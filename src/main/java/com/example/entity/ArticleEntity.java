package com.example.entity;

import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "shared_count")
    private Integer sharedCount;
    @Column(name = "image_id")
    private String imageId;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private ProfileEntity moderator;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private ProfileEntity publisher;
    @Column(name = "status")
    private ArticleStatus status;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "visible")
    private Boolean visible = true;
    @Column(name = "view_count")
    private Integer viewCount;
    @OneToMany()
    @JoinColumn(name = "type")
    private List<ArticleTypeEntity> type;

}
