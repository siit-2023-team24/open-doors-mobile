package com.bookingapptim24.models;

import java.sql.Timestamp;

public class DateRangeDTO {
    private String startDate;
    private String endDate;

    public DateRangeDTO(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRangeDTO() {}

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
        return "DateRange{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
