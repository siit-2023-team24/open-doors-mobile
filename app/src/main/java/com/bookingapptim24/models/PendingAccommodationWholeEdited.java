package com.bookingapptim24.models;

import java.util.ArrayList;
import java.util.List;

public class PendingAccommodationWholeEdited {

    protected Long id;
    protected Long accommodationId;

    protected String name;
    protected String description;
    protected String location;
    protected List<String> amenities;
    protected List<Long> images;
    protected int minGuests;
    protected int maxGuests;
    protected String type;

    protected List<DateRangeDTO> availability;
    protected double price;

    protected boolean isPricePerGuest;

    protected List<SeasonalRateDTO> seasonalRates;

    protected String city;
    protected String country;
    protected String street;
    protected int number;
    protected int deadline;
    protected boolean isAutomatic;
    protected String hostUsername;
    private List<Long> toDeleteImages;

    public PendingAccommodationWholeEdited() {
    }

    public PendingAccommodationWholeEdited(Long id, Long accommodationId, String name, String description, String location, List<String> amenities, List<Long> images, int minGuests, int maxGuests, String type, List<DateRangeDTO> availability, double price, boolean isPricePerGuest, List<SeasonalRateDTO> seasonalRates, String city, String country, String street, int number, int deadline, boolean isAutomatic, String hostUsername, List<Long> toDeleteImages) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.amenities = amenities;
        this.images = images;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.availability = availability;
        this.price = price;
        this.isPricePerGuest = isPricePerGuest;
        this.seasonalRates = seasonalRates;
        this.city = city;
        this.country = country;
        this.street = street;
        this.number = number;
        this.deadline = deadline;
        this.isAutomatic = isAutomatic;
        this.hostUsername = hostUsername;
        this.toDeleteImages = toDeleteImages;
    }

    public PendingAccommodationWholeEdited(Long id, Long accommodationId, String name, String description, String location, List<String> amenities, List<Long> images, int minGuests, int maxGuests, String type, double price, boolean isPricePerGuest, String city, String country, String street, int number, int deadline, boolean isAutomatic, String hostUsername, List<Long> toDeleteImages) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.amenities = amenities;
        this.images = images;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.availability = availability;
        this.price = price;
        this.isPricePerGuest = isPricePerGuest;
        this.seasonalRates = seasonalRates;
        this.city = city;
        this.country = country;
        this.street = street;
        this.number = number;
        this.deadline = deadline;
        this.isAutomatic = isAutomatic;
        this.hostUsername = hostUsername;
        this.toDeleteImages = toDeleteImages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DateRangeDTO> getAvailability() {
        return availability;
    }

    public void setAvailability(List<DateRangeDTO> availability) {
        this.availability = availability;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPricePerGuest() {
        return isPricePerGuest;
    }

    public void setPricePerGuest(boolean pricePerGuest) {
        isPricePerGuest = pricePerGuest;
    }

    public List<SeasonalRateDTO> getSeasonalRates() {
        return seasonalRates;
    }

    public void setSeasonalRates(List<SeasonalRateDTO> seasonalRates) {
        this.seasonalRates = seasonalRates;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }

    public void setAutomatic(boolean automatic) {
        isAutomatic = automatic;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public List<Long> getToDeleteImages() {
        return toDeleteImages;
    }

    public void setToDeleteImages(List<Long> toDeleteImages) {
        this.toDeleteImages = toDeleteImages;
    }


    @Override
    public String toString() {
        return "PendingAccommodationWholeEditedDTO{" +
                "toDeleteImages=" + toDeleteImages +
                ", accommodationId=" + accommodationId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", amenities=" + amenities +
                ", images=" + images +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", type='" + type + '\'' +
//                ", availability=" + availability +
                ", price=" + price +
                ", isPricePerGuest=" + isPricePerGuest +
//                ", seasonalRates=" + seasonalRates +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", deadline=" + deadline +
                ", isAutomatic=" + isAutomatic +
                ", hostUsername='" + hostUsername + '\'' +
                '}';
    }
}