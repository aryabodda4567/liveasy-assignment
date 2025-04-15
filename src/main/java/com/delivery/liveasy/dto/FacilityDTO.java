package com.delivery.liveasy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Facility.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {
    
    @NotBlank(message = "Loading point is required")
    private String loadingPoint;
    
    @NotBlank(message = "Unloading point is required")
    private String unloadingPoint;
    
    @NotNull(message = "Loading date is required")
    private LocalDateTime loadingDate;
    
    @NotNull(message = "Unloading date is required")
    private LocalDateTime unloadingDate;
}
