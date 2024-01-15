package com.bookingapptim24.models;

import java.util.ArrayList;
import java.util.List;

public class PendingAccommodationWholeEdited extends PendingAccommodationWhole {

    private List<Long> toDeleteImages;

    public PendingAccommodationWholeEdited() {
    }

    public PendingAccommodationWholeEdited(Long id, Long accommodationId, String name, String description, String location, List<String> amenities, List<Long> images, int minGuests, int maxGuests, String type, double price, boolean isPricePerGuest, String city, String country, String street, int number, int deadline, boolean isAutomatic, String hostUsername, List<Long> toDeleteImages) {
        super(id, accommodationId, name, description, location, amenities, images, minGuests, maxGuests, type, price, isPricePerGuest, city, country, street, number, deadline, isAutomatic, hostUsername);
        this.toDeleteImages = toDeleteImages;
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