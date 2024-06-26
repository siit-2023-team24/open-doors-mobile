package com.bookingapptim24.models.reviews;

import java.sql.Timestamp;

public class ReviewDetails {
    protected Long id;
    protected int rating;
    protected String comment;
    protected Timestamp timestamp;
    protected String authorUsername;

    protected Long imageId;
    protected boolean isProcessed;

    public ReviewDetails() {
    }

    public ReviewDetails(Long id, int rating, String comment, Timestamp timestamp, String authorUsername, Long imageId, boolean isProcessed) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.authorUsername = authorUsername;
        this.imageId = imageId;
        this.isProcessed = isProcessed;
    }

    public boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
