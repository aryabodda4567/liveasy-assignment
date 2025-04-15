package com.delivery.liveasy.repository;

import com.delivery.liveasy.model.Booking;
import com.delivery.liveasy.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    
    /**
     * Find bookings by load ID.
     *
     * @param loadId the load ID
     * @return list of bookings
     */
    List<Booking> findByLoadId(UUID loadId);
    
    /**
     * Find bookings by transporter ID.
     *
     * @param transporterId the transporter ID
     * @return list of bookings
     */
    List<Booking> findByTransporterId(String transporterId);
    
    /**
     * Find bookings by load ID and transporter ID.
     *
     * @param loadId the load ID
     * @param transporterId the transporter ID
     * @return optional booking
     */
    Optional<Booking> findByLoadIdAndTransporterId(UUID loadId, String transporterId);
    
    /**
     * Find bookings by load ID and status.
     *
     * @param loadId the load ID
     * @param status the booking status
     * @return list of bookings
     */
    List<Booking> findByLoadIdAndStatus(UUID loadId, BookingStatus status);
}
