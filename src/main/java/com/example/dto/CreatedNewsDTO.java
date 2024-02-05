package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatedNewsDTO {
    private Long orderNumber;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String name;
}
