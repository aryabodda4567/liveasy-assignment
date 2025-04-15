package com.delivery.liveasy.model;
import com.delivery.liveasy.model.enums.BookingStatus;
import jakarta.persistence.*;
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
 * Entity class representing a booking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @NotNull(message = "Load ID is required")
    @Column(name = "load_id", nullable = false)
    private UUID loadId;
    
    @NotBlank(message = "Transporter ID is required")
    @Column(name = "transporter_id", nullable = false)
    private String transporterId;
    
    @NotNull(message = "Proposed rate is required")
    @Positive(message = "Proposed rate must be positive")
    @Column(name = "proposed_rate", nullable = false)
    private Double proposedRate;
    
    @Column(name = "comment")
    private String comment;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Load load;
}
