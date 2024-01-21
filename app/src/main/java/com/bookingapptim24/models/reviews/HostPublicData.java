package com.bookingapptim24.models.reviews;

import java.util.List;

public class HostPublicData {
    private String username;
    private String firstName;
    private String lastName;

    private Long imageId;

    private List<ReviewDetails> reviews;
    private boolean isReviewable;
    public Long getImageId() {
        return imageId;
    }

    public HostPublicData() {
    }

    public HostPublicData(String username, String firstName, String lastName, Long imageId, List<ReviewDetails> reviews, boolean isReviewable) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageId = imageId;
        this.reviews = reviews;
        this.isReviewable = isReviewable;
    }

    public boolean getIsReviewable() {
        return isReviewable;
    }

    public void setIsReviewable(boolean isReviewable) {
        this.isReviewable = isReviewable;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<ReviewDetails> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDetails> reviews) {
        this.reviews = reviews;
    }

    public Double getAverageRating() {
        Double sum = 0.0;
        for (ReviewDetails reviewDetails : reviews) {
            sum += reviewDetails.getRating();
        }
        if (sum==0) return null;
        sum /= reviews.size();
        return sum;
    }
}
