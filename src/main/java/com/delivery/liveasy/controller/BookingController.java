package com.delivery.liveasy.controller;

import com.delivery.liveasy.dto.BookingDTO;
import com.delivery.liveasy.model.enums.BookingStatus;
import com.delivery.liveasy.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Booking operations.
 */
@RestController
@RequestMapping("/booking")
@Slf4j
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Create a new booking.
     *
     * @param bookingDTO the booking DTO
     * @return the created booking DTO
     */
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        log.info("REST request to create a new booking");
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /**
     * Get all bookings with optional filtering.
     *
     * @param loadId the load ID (optional)
     * @param transporterId the transporter ID (optional)
     * @return list of booking DTOs
     */
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings(
            @RequestParam(required = false) UUID loadId,
            @RequestParam(required = false) String transporterId) {

        log.info("REST request to get bookings with filters: loadId={}, transporterId={}",
                loadId, transporterId);

        List<BookingDTO> bookings;

        if (loadId != null) {
            bookings = bookingService.getBookingsByLoadId(loadId);
        } else if (transporterId != null) {
            bookings = bookingService.getBookingsByTransporterId(transporterId);
        } else {
            bookings = bookingService.getAllBookings();
        }

        return ResponseEntity.ok(bookings);
    }

    /**
     * Get booking by ID.
     *
     * @param bookingId the booking ID
     * @return the booking DTO
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable UUID bookingId) {
        log.info("REST request to get booking with ID: {}", bookingId);
        BookingDTO booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    /**
     * Update booking.
     *
     * @param bookingId the booking ID
     * @param bookingDTO the booking DTO
     * @return the updated booking DTO
     */
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable UUID bookingId,
            @Valid @RequestBody BookingDTO bookingDTO) {

        log.info("REST request to update booking with ID: {}", bookingId);
        BookingDTO updatedBooking = bookingService.updateBooking(bookingId, bookingDTO);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Delete booking.
     *
     * @param bookingId the booking ID
     * @return no content
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        log.info("REST request to delete booking with ID: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update booking status.
     *
     * @param bookingId the booking ID
     * @param status the booking status
     * @return the updated booking DTO
     */
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable UUID bookingId,
            @RequestParam BookingStatus status) {

        log.info("REST request to update booking status to {} for booking with ID: {}",
                status, bookingId);

        BookingDTO updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(updatedBooking);
    }
}
