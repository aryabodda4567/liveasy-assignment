package com.delivery.liveasy.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Embeddable class representing facility details for a load.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Facility {
    
    @NotBlank(message = "Loading point is required")
    private String loadingPoint;
    
    @NotBlank(message = "Unloading point is required")
    private String unloadingPoint;
    
    @NotNull(message = "Loading date is required")
    private LocalDateTime loadingDate;
    
    @NotNull(message = "Unloading date is required")
    private LocalDateTime unloadingDate;
}
