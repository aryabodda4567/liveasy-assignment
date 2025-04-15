package com.delivery.liveasy.service.impl;

import com.delivery.liveasy.dto.BookingDTO;
import com.delivery.liveasy.exception.BadRequestException;
import com.delivery.liveasy.exception.ResourceNotFoundException;
import com.delivery.liveasy.model.Booking;
import com.delivery.liveasy.model.Load;
import com.delivery.liveasy.model.enums.BookingStatus;
import com.delivery.liveasy.model.enums.LoadStatus;
import com.delivery.liveasy.repository.BookingRepository;
import com.delivery.liveasy.repository.LoadRepository;
import com.delivery.liveasy.service.BookingService;
import com.delivery.liveasy.service.LoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of BookingService.
 */
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LoadRepository loadRepository;

    @Autowired
    private LoadService loadService;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        log.info("Creating booking for load: {} and transporter: {}",
                bookingDTO.getLoadId(), bookingDTO.getTransporterId());

        // Check if load exists
        Load load = loadRepository.findById(bookingDTO.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", bookingDTO.getLoadId()));

        // Check if load is already cancelled
        if (load.getStatus() == LoadStatus.CANCELLED) {
            throw new BadRequestException("Cannot create booking for a cancelled load");
        }

        // Create booking
        Booking booking = mapToEntity(bookingDTO);
        booking.setStatus(BookingStatus.PENDING); // Default status

        Booking savedBooking = bookingRepository.save(booking);

        // Update load status to BOOKED
        loadService.updateLoadStatus(load.getId(), LoadStatus.BOOKED);

        log.info("Booking created with ID: {}", savedBooking.getId());

        return mapToDTO(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        log.info("Fetching all bookings");

        return bookingRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByLoadId(UUID loadId) {
        log.info("Fetching bookings for load: {}", loadId);

        // Check if load exists
        if (!loadRepository.existsById(loadId)) {
            throw new ResourceNotFoundException("Load", "id", loadId);
        }

        return bookingRepository.findByLoadId(loadId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByTransporterId(String transporterId) {
        log.info("Fetching bookings for transporter: {}", transporterId);

        return bookingRepository.findByTransporterId(transporterId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getBookingById(UUID id) {
        log.info("Fetching booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        return mapToDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(UUID id, BookingDTO bookingDTO) {
        log.info("Updating booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        // Check if load exists
        if (!loadRepository.existsById(bookingDTO.getLoadId())) {
            throw new ResourceNotFoundException("Load", "id", bookingDTO.getLoadId());
        }

        // Update fields
        booking.setLoadId(bookingDTO.getLoadId());
        booking.setTransporterId(bookingDTO.getTransporterId());
        booking.setProposedRate(bookingDTO.getProposedRate());
        booking.setComment(bookingDTO.getComment());

        // Don't update status here, use updateBookingStatus method

        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking updated with ID: {}", updatedBooking.getId());

        return mapToDTO(updatedBooking);
    }

    @Override
    @Transactional
    public void deleteBooking(UUID id) {
        log.info("Deleting booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        // Get the load
        Load load = loadRepository.findById(booking.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", booking.getLoadId()));

        // Delete booking
        bookingRepository.delete(booking);

        // Update load status to CANCELLED
        loadService.updateLoadStatus(load.getId(), LoadStatus.CANCELLED);

        log.info("Booking deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public BookingDTO updateBookingStatus(UUID id, BookingStatus status) {
        log.info("Updating booking status to {} for booking with ID: {}", status, id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        booking.setStatus(status);

        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking status updated to {} for booking with ID: {}", status, id);

        return mapToDTO(updatedBooking);
    }

    /**
     * Map Booking entity to BookingDTO.
     *
     * @param booking the booking entity
     * @return the booking DTO
     */
    private BookingDTO mapToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setLoadId(booking.getLoadId());
        bookingDTO.setTransporterId(booking.getTransporterId());
        bookingDTO.setProposedRate(booking.getProposedRate());
        bookingDTO.setComment(booking.getComment());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setRequestedAt(booking.getRequestedAt());

        // Load details if available
        if (booking.getLoad() != null) {
            bookingDTO.setLoad(loadService.getLoadById(booking.getLoadId()));
        }

        return bookingDTO;
    }

    /**
     * Map BookingDTO to Booking entity.
     *
     * @param bookingDTO the booking DTO
     * @return the booking entity
     */
    private Booking mapToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setLoadId(bookingDTO.getLoadId());
        booking.setTransporterId(bookingDTO.getTransporterId());
        booking.setProposedRate(bookingDTO.getProposedRate());
        booking.setComment(bookingDTO.getComment());

        return booking;
    }
}
