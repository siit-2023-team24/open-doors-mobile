package com.bookingapptim24.models.reviews;

public class NewReview {
    private int rating;
    private String comment;
    private Long authorId;
    private Long recipientId;

    public NewReview() {
    }

    public NewReview(int rating, String comment, Long authorId, Long recipientId) {
        this.rating = rating;
        this.comment = comment;
        this.authorId = authorId;
        this.recipientId = recipientId;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}