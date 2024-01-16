package com.bookingapptim24.models;

import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;

import java.util.HashSet;
import java.util.Set;

public class SearchAndFilterAccommodations {
    private String location;
    private Integer guestNumber;
    private String startDate;
    private String endDate;
    private Double startPrice;
    private Double endPrice;
    private Set<AccommodationType> types;
    private Set<Amenity> amenities;

    public SearchAndFilterAccommodations() {
        this.types = new HashSet<>();
        this.amenities = new HashSet<>();
    }

    public SearchAndFilterAccommodations(String location, Integer guestNumber, String startDate, String endDate,
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
