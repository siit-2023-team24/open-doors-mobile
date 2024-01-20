package com.bookingapptim24.models;

import java.sql.Timestamp;

public class AccommodationSeasonalRate {

    private Long accommodationId;
    private String startDate;
    private String endDate;

    public AccommodationSeasonalRate(Long accommodationId, String startDate, String endDate) {
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

    @Override
    public String toString() {
        return "AccommodationSeasonalRateDTO{" +
                "accommodationId=" + accommodationId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
