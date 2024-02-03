package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleCreateDTO {
    private String title;
    private String description;
    private String content;
    private String imageId;
    private Integer regionId;
    private Integer categoryId;
    private List<ArticleTypeCrudeDTO> articleType;
}
