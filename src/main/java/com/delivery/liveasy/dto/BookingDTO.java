package com.delivery.liveasy.dto;

import com.delivery.liveasy.model.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Booking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    
    private UUID id;
    
    @NotNull(message = "Load ID is required")
    private UUID loadId;
    
    @NotBlank(message = "Transporter ID is required")
    private String transporterId;
    
    @NotNull(message = "Proposed rate is required")
    @Positive(message = "Proposed rate must be positive")
    private Double proposedRate;
    
    private String comment;
    
    private BookingStatus status;
    
    private LocalDateTime requestedAt;
    
    private LoadDTO load;
}
