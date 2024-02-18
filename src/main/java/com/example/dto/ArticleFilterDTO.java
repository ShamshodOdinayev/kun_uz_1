package com.example.dto;

import com.example.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ArticleFilterDTO {
    private String id;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
    private String title;
    private Integer regionId;
    private Integer categoryId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate fromPublishedDate;
    private LocalDate toPublishedDate;
}
