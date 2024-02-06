package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCreateDTO {
    private String title;
    private String description;
    private String content;
    private String photoId;
    private Integer regionId;
    private Integer categoryId;
    private List<Integer> articleType;
}
