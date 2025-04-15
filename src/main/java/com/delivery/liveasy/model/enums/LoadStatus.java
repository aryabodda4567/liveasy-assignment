package com.delivery.liveasy.model.enums;

/**
 * Enum representing the status of a load.
 */
public enum LoadStatus {
    POSTED,    // Initial status when a load is created
    BOOKED,    // Status when a load is booked
    CANCELLED  // Status when a booking is deleted
}
