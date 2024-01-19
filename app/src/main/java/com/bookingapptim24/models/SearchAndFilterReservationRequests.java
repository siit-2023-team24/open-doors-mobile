package com.bookingapptim24.models;

import com.bookingapptim24.models.enums.ReservationRequestStatus;

import java.sql.Timestamp;

public class SearchAndFilterReservationRequests {

    String accommodationName;
    String startDate;
    String endDate;
    ReservationRequestStatus status;

    public SearchAndFilterReservationRequests() {}

    public SearchAndFilterReservationRequests(String accommodationName, String startDate, String endDate, ReservationRequestStatus status) {
        this.accommodationName = accommodationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
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

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationRequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReservationRequestSearchAndFilterDTO{" +
                "accommodationName='" + accommodationName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
