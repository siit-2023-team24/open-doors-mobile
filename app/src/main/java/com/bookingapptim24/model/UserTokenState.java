package com.bookingapptim24.model;

public class UserTokenState {
    private String accessToken;
    private int expiresIn;
    private String message;

    public UserTokenState(String accessToken, int expiresIn, String message) {
        this.accessToken=accessToken;
        this.expiresIn=expiresIn;
        this.message=message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
