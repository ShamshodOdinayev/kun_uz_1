package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;

    @NotNull(message = "OrderNumber required")
    private Integer orderNumber;

    @NotBlank
    @NotNull(message = "OrderNumber required")
    private String nameUz;

    @NotBlank
    @Size(min = 10, max = 200, message = "NameUz must be between 10 and 200 characters")
    private String nameRu;

    @NotBlank
    @Size(min = 10, max = 200, message = "NameUz must be between 10 and 200 characters")
    private String nameEn;
}
