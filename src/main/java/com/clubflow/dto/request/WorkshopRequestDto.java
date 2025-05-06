package com.clubflow.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkshopRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private Long memberId;

    @NotBlank(message = "Reason is required")
    private String reason;
}