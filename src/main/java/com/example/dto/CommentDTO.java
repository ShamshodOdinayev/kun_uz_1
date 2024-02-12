package com.example.dto;

import com.example.entity.ProfileEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;
    private Integer profileId;
    private String content;
    private String articleId;
    private Integer replyId;
}
