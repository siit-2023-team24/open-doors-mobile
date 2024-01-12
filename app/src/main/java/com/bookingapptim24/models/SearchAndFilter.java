package com.bookingapptim24.models;

import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;

import java.sql.Timestamp;
import java.util.Set;

public class SearchAndFilter {
    private String location;
    private Integer guestNumber;
    private Timestamp startDate;
    private Timestamp endDate;
    private Double startPrice;
    private Double endPrice;
    private Set<AccommodationType> types;
    private Set<Amenity> amenities;

    public SearchAndFilter() {}

    public SearchAndFilter(String location, Integer guestNumber, Timestamp startDate, Timestamp endDate,
                              Double startPrice, Double endPrice, Set<AccommodationType> types, Set<Amenity> amenities) {
        this.location = location;
        this.guestNumber = guestNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.types = types;
        this.amenities = amenities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(Integer guestNumber) {
        this.guestNumber = guestNumber;
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

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice = endPrice;
    }

    public Set<AccommodationType> getTypes() {
        return types;
    }

    public void setTypes(Set<AccommodationType> types) {
        this.types = types;
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "SearchAndFilterDTO{" +
                "location=" + location +
                ", guestNumber=" + guestNumber +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startPrice=" + startPrice +
                ", endPrice=" + endPrice +
                ", types=" + types +
                ", amenities=" + amenities +
                '}';
    }
}
