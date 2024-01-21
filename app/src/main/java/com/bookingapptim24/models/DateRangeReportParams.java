package com.bookingapptim24.models;

import java.sql.Timestamp;

public class DateRangeReportParams {
    private Long hostId;
    private String startDate;
    private String endDate;

    public DateRangeReportParams(Long hostId, String startDate, String endDate) {
        this.hostId = hostId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
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
        return "FinancialReportDateRangeParamsDTO{" +
                "hostId=" + hostId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
