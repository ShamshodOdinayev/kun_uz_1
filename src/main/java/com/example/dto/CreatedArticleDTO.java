package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatedArticleDTO {
    /*title,description,content,image_id, region_id,category_id, articleType(array))*/
    private String title;
    private String description;
    private String content;
    private String imageId;
    private Integer regionId;
    private Integer categoryId;
    private Integer [] newsType;
}
