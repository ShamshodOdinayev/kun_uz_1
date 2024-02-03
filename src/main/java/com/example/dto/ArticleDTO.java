package com.example.dto;

import com.example.entity.ArticleTypeEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private String imageId;
    private RegionEntity region;
    private CategoryEntity category;
    private ProfileEntity moderator;
    private ProfileEntity publisher;
    private ArticleStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private Boolean visible;
    private Integer viewCount;
    private List<ArticleTypeEntity> type;
}
