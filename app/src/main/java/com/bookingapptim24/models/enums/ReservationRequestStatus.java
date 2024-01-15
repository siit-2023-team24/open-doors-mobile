package com.bookingapptim24.models.enums;

public enum ReservationRequestStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DENIED("denied"),
    CANCELLED("cancelled"),
    DELETED("deleted");

    private final String status;

    ReservationRequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
