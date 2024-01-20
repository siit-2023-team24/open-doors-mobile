package com.bookingapptim24.models;

public class AccommodationFavorites {
    private Long accommodationId;
    private Long guestId;

    public AccommodationFavorites(Long accommodationId, Long guestId) {
        this.accommodationId = accommodationId;
        this.guestId = guestId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    @Override
    public String toString() {
        return "AccommodationFavoritesDTO{" +
                "accommodationId=" + accommodationId +
                ", guestId=" + guestId +
                '}';
    }
}
