package com.bookingapptim24.models;

public class AccommodationSearchDTO {
    private Long id;
    private Long image;
    private String name;
    private Double averageRating;
    private double price;
    private boolean isPricePerGuest;
    private Double totalPrice;
    private String city;
    private String country;
    private boolean isFavoriteForGuest;

    public AccommodationSearchDTO() {}

    public AccommodationSearchDTO(Long id, Long image, String name, Double averageRating, double price, boolean isPricePerGuest, Double totalPrice, String city, String country, boolean isFavoriteForGuest) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.averageRating = averageRating;
        this.price = price;
        this.isPricePerGuest = isPricePerGuest;
        this.totalPrice = totalPrice;
        this.city = city;
        this.country = country;
        this.isFavoriteForGuest = isFavoriteForGuest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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

    public boolean isFavoriteForGuest() {
        return isFavoriteForGuest;
    }

    public void setFavoriteForGuest(boolean favoriteForGuest) {
        isFavoriteForGuest = favoriteForGuest;
    }

    @Override
    public String toString() {
        return "AccommodationSearchDTO{" +
                "id=" + id +
                ", image=" + image +
                ", name='" + name + '\'' +
                ", averageRating=" + averageRating +
                ", price=" + price +
                ", isPricePerGuest=" + isPricePerGuest +
                ", totalPrice=" + totalPrice +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", isFavoriteForGuest=" + isFavoriteForGuest +
                '}';
    }
}
