package com.clubflow.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuideDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Club is required")
    private String club;

    @NotBlank(message = "Link is required")
    private String link;
}