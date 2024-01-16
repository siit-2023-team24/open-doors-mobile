package com.bookingapptim24.models;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.models.enums.AccommodationType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccommodationWithTotalPrice {
    private Long id;
    private String name;
    private String description;
    private String location;
    private List<String> amenities;
    private List<Long> images;
    private int minGuests;
    private int maxGuests;
    private AccommodationType accommodationType;
    private List<DateRange> availability;
    private double price;
    private boolean isPricePerGuest;
    private Double totalPrice;
    private Double averageRating;
    private String host;
    private List<SeasonalRate> seasonalRates;
    private String country;
    private String city;
    private String street;
    private int number;
    private boolean isFavoriteForGuest;
    private Long hostId;

    public AccommodationWithTotalPrice() {}

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<Long> getImages() {
        return images;
    }

    public void setImages(List<Long> images) {
        this.images = images;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public List<DateRange> getAvailability() {
        return availability;
    }

    public void setAvailability(List<DateRange> availability) {
        this.availability = availability;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<SeasonalRate> getSeasonalRates() {
        return seasonalRates;
    }

    public void setSeasonalRates(List<SeasonalRate> seasonalRates) {
        this.seasonalRates = seasonalRates;
    }

    public boolean getIsPricePerGuest() {
        return isPricePerGuest;
    }

    public void setIsPricePerGuest(boolean pricePerGuest) {
        isPricePerGuest = pricePerGuest;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getIsFavoriteForGuest() {
        return isFavoriteForGuest;
    }

    public void setIsFavoriteForGuest(boolean isFavoriteForGuest) {
        this.isFavoriteForGuest = isFavoriteForGuest;
    }
    public boolean isDateAvailable(long targetTime) {
        if (availability != null && !availability.isEmpty()) {
            for (DateRange dateRange : availability) {
                Timestamp startDate = dateRange.getStartDate();
                Timestamp endDate = dateRange.getEndDate();

                if (startDate != null && endDate != null) {
                    long startTime = startDate.getTime();
                    long endTime = endDate.getTime();

                    // Check if the target date is within the range
                    if (targetTime >= startTime && targetTime <= endTime) {
                        return true; // Date is available
                    }
                }
            }
        }
        return false; // Date is not available or availability information is not provided
    }
}
