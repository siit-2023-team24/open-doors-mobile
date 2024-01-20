package com.bookingapptim24.models;

import java.sql.Timestamp;

public class MakeReservationRequest {
    Long accommodationId;
    Long guestId;
    String startDate;
    String endDate;
    int numberOfGuests;
    Double totalPrice;

    public MakeReservationRequest() {}

    public MakeReservationRequest(Long accommodationId, Long guestId, String startDate, String endDate, int numberOfGuests, Double totalPrice) {
        this.accommodationId = accommodationId;
        this.guestId = guestId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "MakeReservationRequestDTO{" +
                "accommodationId=" + accommodationId +
                ", guestId=" + guestId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", numberOfGuests=" + numberOfGuests +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
