package com.bookingapptim24.models.reviews;

import java.sql.Timestamp;

public class HostReviewWhole extends NewReview {
    private Long id;
    private Timestamp timestamp;

    private boolean reported;

    public HostReviewWhole() {
    }

    public HostReviewWhole(int rating, String comment, Long authorId, Long recipientId, Long id, Timestamp timestamp, boolean reported) {
        super(rating, comment, authorId, recipientId);
        this.id = id;
        this.timestamp = timestamp;
        this.reported = reported;
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

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
