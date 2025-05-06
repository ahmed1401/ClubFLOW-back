package com.clubflow.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Club is required")
    private String club;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Time is required")
    private String time;

    @NotBlank(message = "Location is required")
    private String location;
}