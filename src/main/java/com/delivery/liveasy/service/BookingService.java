package com.delivery.liveasy.service;

import com.delivery.liveasy.dto.BookingDTO;
import com.delivery.liveasy.model.enums.BookingStatus;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Booking operations.
 */
public interface BookingService {
    
    /**
     * Create a new booking.
     *
     * @param bookingDTO the booking DTO
     * @return the created booking DTO
     */
    BookingDTO createBooking(BookingDTO bookingDTO);
    
    /**
     * Get all bookings.
     *
     * @return list of booking DTOs
     */
    List<BookingDTO> getAllBookings();
    
    /**
     * Get bookings by load ID.
     *
     * @param loadId the load ID
     * @return list of booking DTOs
     */
    List<BookingDTO> getBookingsByLoadId(UUID loadId);
    
    /**
     * Get bookings by transporter ID.
     *
     * @param transporterId the transporter ID
     * @return list of booking DTOs
     */
    List<BookingDTO> getBookingsByTransporterId(String transporterId);
    
    /**
     * Get booking by ID.
     *
     * @param id the booking ID
     * @return the booking DTO
     */
    BookingDTO getBookingById(UUID id);
    
    /**
     * Update booking.
     *
     * @param id the booking ID
     * @param bookingDTO the booking DTO
     * @return the updated booking DTO
     */
    BookingDTO updateBooking(UUID id, BookingDTO bookingDTO);
    
    /**
     * Delete booking.
     *
     * @param id the booking ID
     */
    void deleteBooking(UUID id);
    
    /**
     * Update booking status.
     *
     * @param id the booking ID
     * @param status the booking status
     * @return the updated booking DTO
     */
    BookingDTO updateBookingStatus(UUID id, BookingStatus status);
}
