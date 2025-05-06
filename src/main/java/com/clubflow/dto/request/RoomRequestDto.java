package com.clubflow.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequestDto {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Building is required")
    private String building;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Start time is required")
    private String startTime;

    @NotNull(message = "Duration is required")
    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 240, message = "Duration must be at most 240 minutes")
    private Integer duration;

    @NotBlank(message = "Purpose is required")
    private String purpose;
}