package com.bookingapptim24.models.reviews;

import java.util.List;

public class AccommodationReviews {
    private List<ReviewDetails> reviews;

    private boolean isReviewable;

    private Double averageRating;

    private ReviewDetails unapprovedReview;

    public AccommodationReviews() {
        this.isReviewable = false;
    };

    public AccommodationReviews(List<ReviewDetails> reviews, boolean isReviewable, ReviewDetails unapprovedReview) {
        this.reviews = reviews;
        this.isReviewable = isReviewable;
        this.averageRating = null;
        this.unapprovedReview = unapprovedReview;
    }

    public List<ReviewDetails> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDetails> reviews) {
        this.reviews = reviews;
    }

    public boolean getIsReviewable() {
        return isReviewable;
    }

    public void setIsReviewable(boolean isReviewable) {
        this.isReviewable = isReviewable;
    }

    public boolean isReviewable() {
        return isReviewable;
    }

    public void setReviewable(boolean reviewable) {
        isReviewable = reviewable;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public ReviewDetails getUnapprovedReview() {
        return unapprovedReview;
    }

    public void setUnapprovedReview(ReviewDetails unapprovedReview) {
        this.unapprovedReview = unapprovedReview;
    }
}