package com.bookingapptim24.models;

public class SeasonalRateDTO {
    private double price;
    private DateRangeDTO period;

    public SeasonalRateDTO() {

    }
    public SeasonalRateDTO(double value, DateRangeDTO period) {
        this.price = value;
        this.period = period;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DateRangeDTO getPeriod() {
        return period;
    }

    public void setPeriod(DateRangeDTO period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                ", period=" + period +
                '}';
    }
}
