package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetTheLastArticleNotListedDTO {
    private String[] articles;
    private Integer size;
}
