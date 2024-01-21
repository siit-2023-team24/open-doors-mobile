package com.bookingapptim24.models;

import com.bookingapptim24.models.enums.ReservationRequestStatus;

import java.sql.Timestamp;

public class ReservationRequestForGuest {
    protected Long id;
    protected Long imageId;
    protected String accommodationName;
    protected Timestamp startDate;
    protected Timestamp endDate;
    protected int guestNumber;
    protected double totalPrice;
    protected ReservationRequestStatus status;
    protected Timestamp timestamp;

    protected String hostUsername;

    public ReservationRequestForGuest() {
    }

    public ReservationRequestForGuest(Long id, Long imageId, String accommodationName, Timestamp startDate, Timestamp endDate, int guestNumber, double totalPrice, ReservationRequestStatus status, Timestamp timestamp, String hostUsername) {
        this.id = id;
        this.imageId = imageId;
        this.accommodationName = accommodationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestNumber = guestNumber;
        this.totalPrice = totalPrice;
        this.status = status;
        this.timestamp = timestamp;
        this.hostUsername = hostUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationRequestStatus status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    @Override
    public String toString() {
        return "ReservationRequestForGuest{" +
                "id=" + id +
                ", imageId=" + imageId +
                ", accommodationName='" + accommodationName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", guestNumber=" + guestNumber +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
