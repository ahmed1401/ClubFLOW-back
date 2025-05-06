package com.clubflow.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaterialRequestDto {

    @NotBlank(message = "Club name is required")
    private String club;

    @NotBlank(message = "Material name is required")
    private String name;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Requested date is required")
    private String requestedDate;

    @NotBlank(message = "Reason is required")
    private String reason;

    public MaterialRequestDto(String techClub, String projector, int i, LocalDate now, String presentationNeeds) {

    }
}