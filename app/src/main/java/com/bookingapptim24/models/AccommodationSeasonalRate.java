package com.bookingapptim24.models;

import java.sql.Timestamp;

public class AccommodationSeasonalRate {

    private Long accommodationId;
    private Timestamp startDate;
    private Timestamp endDate;

    public AccommodationSeasonalRate(Long accommodationId, Timestamp startDate, Timestamp endDate) {
        this.accommodationId = accommodationId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
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

    @Override
    public String toString() {
        return "AccommodationSeasonalRateDTO{" +
                "accommodationId=" + accommodationId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
