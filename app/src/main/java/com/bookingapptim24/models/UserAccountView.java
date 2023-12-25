package com.bookingapptim24.models;

public class UserAccountView extends User {

    private String username;

    private String role;

    public UserAccountView() {
    }

    public UserAccountView(Long id, String firstName, String lastName, String phone, String street, int number, String city, String country, Long imageId, String username, String role) {
        super(id, firstName, lastName, phone, street, number, city, country, imageId);
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserAccountView{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}
