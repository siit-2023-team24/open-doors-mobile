package com.bookingapptim24.models;

public class UserAccount extends User {
    private String username;
    private String password;
    private String role;


    public UserAccount() {

    }

    public UserAccount(Long id, String firstName, String lastName, String phone, String street, int number, String city, String country, Long imageId, String email, String password, String role) {
        super(id, firstName, lastName, phone, street, number, city, country, imageId);
        this.username = email;
        this.password = password;
        this.role = role;
    }

    public UserAccount(String email, String password, String role) {
        this.username = email;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccountDTO{" +
                "email='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
