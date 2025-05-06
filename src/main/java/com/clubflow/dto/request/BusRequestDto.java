package com.clubflow.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusRequestDto {

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Departure date is required")
    private String departureDate;

    private String returnDate;

    @NotNull(message = "Number of passengers is required")
    @Min(value = 1, message = "Number of passengers must be at least 1")
    private Integer numberOfPassengers;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    private String additionalNotes;
}