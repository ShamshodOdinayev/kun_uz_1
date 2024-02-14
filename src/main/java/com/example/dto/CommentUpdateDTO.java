package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentUpdateDTO {
    private String content;
    private String articleId;
    private Integer replyId;
}
