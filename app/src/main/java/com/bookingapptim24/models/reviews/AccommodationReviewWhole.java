package com.bookingapptim24.models.reviews;

import java.sql.Timestamp;

public class AccommodationReviewWhole extends NewReview {
    private Long id;
    private Timestamp timestamp;

    private boolean approved;

    public AccommodationReviewWhole() {
    }

    public AccommodationReviewWhole(int rating, String comment, Long authorId, Long recipientId, Long id, Timestamp timestamp, boolean approved) {
        super(rating, comment, authorId, recipientId);
        this.id = id;
        this.timestamp = timestamp;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}