package com.bookingapptim24.models;

public class DateRangeReport {

    private Long accommodationId;
    private String accommodationName;
    private int numOfReservations;
    private double profit;

    public DateRangeReport() {
    }

    public DateRangeReport(Long accommodationId, String accommodationName, int numOfReservations, double profit) {
        this.accommodationName = accommodationName;
        this.accommodationId = accommodationId;
        this.numOfReservations = numOfReservations;
        this.profit = profit;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public int getNumOfReservations() {
        return numOfReservations;
    }

    public void setNumOfReservations(int numOfReservations) {
        this.numOfReservations = numOfReservations;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "DateRangeReport{" +
                "accommodationId=" + accommodationId +
                ", accommodationName='" + accommodationName + '\'' +
                ", numOfReservations=" + numOfReservations +
                ", profit=" + profit +
                '}';
    }
}
