package com.bookingapptim24.models;

import java.sql.Timestamp;

public class SeasonalRatesPricing {

    private Double price;
    private String startDate;
    private String endDate;
    private Integer numberOfNights;

    public SeasonalRatesPricing(Double price, String startDate, String endDate, Integer numberOfNights) {
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfNights = numberOfNights;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        return "SeasonalRatePricingDTO{" +
                "price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", numberOfNights=" + numberOfNights +
                '}';
    }
}
