package com.bookingapptim24.model;

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String street;
    private int number;
    private String city;
    private String country;
    private Long imageId;

    public UserDTO(Long id, String firstName, String lastName, String phone, String street, int number, String city, String country, Long imageId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.street = street;
        this.number = number;
        this.city = city;
        this.country = country;
        this.imageId = imageId;
    }

    public UserDTO() {

    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long image) {
        this.imageId = image;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", imageId='" + imageId + '\'' +
                '}';
    }
}