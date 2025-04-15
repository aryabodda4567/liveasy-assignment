package com.delivery.liveasy.dto;

import com.delivery.liveasy.model.enums.LoadStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Load.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadDTO {
    
    private UUID id;
    
    @NotBlank(message = "Shipper ID is required")
    private String shipperId;
    
    @Valid
    @NotNull(message = "Facility details are required")
    private FacilityDTO facility;
    
    @NotBlank(message = "Product type is required")
    private String productType;
    
    @NotBlank(message = "Truck type is required")
    private String truckType;
    
    @NotNull(message = "Number of trucks is required")
    @Min(value = 1, message = "Number of trucks must be at least 1")
    private Integer noOfTrucks;
    
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;
    
    private String comment;
    
    private LocalDateTime datePosted;
    
    private LoadStatus status;
}
