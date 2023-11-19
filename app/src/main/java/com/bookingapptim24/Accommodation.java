package com.bookingapptim24;

public class Accommodation {
    private String name;
    private int imageResource;
    private double averageRating;
    private double price;

    public Accommodation(String name, int imageResource, double averageRating, double price) {
        this.name = name;
        this.imageResource = imageResource;
        this.averageRating = averageRating;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public double getPrice() { return price; }
}
