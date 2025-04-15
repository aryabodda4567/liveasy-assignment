package com.delivery.liveasy.model;

import com.delivery.liveasy.model.enums.LoadStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing a load.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loads")
public class Load {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @NotBlank(message = "Shipper ID is required")
    @Column(name = "shipper_id", nullable = false)
    private String shipperId;
    
    @Valid
    @Embedded
    private Facility facility;
    
    @NotBlank(message = "Product type is required")
    @Column(name = "product_type", nullable = false)
    private String productType;
    
    @NotBlank(message = "Truck type is required")
    @Column(name = "truck_type", nullable = false)
    private String truckType;
    
    @NotNull(message = "Number of trucks is required")
    @Min(value = 1, message = "Number of trucks must be at least 1")
    @Column(name = "no_of_trucks", nullable = false)
    private Integer noOfTrucks;
    
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    @Column(name = "weight", nullable = false)
    private Double weight;
    
    @Column(name = "comment")
    private String comment;
    
    @CreationTimestamp
    @Column(name = "date_posted", nullable = false, updatable = false)
    private LocalDateTime datePosted;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoadStatus status = LoadStatus.POSTED;
}
