package com.example.dto;

import com.example.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleLikeDTO {
    private Integer id;
    private Integer profileId;
    private String articleId;
    private LikeStatus status;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;
    private Boolean visible = true;

}

