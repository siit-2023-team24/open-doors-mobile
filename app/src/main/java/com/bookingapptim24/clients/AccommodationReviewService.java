package com.bookingapptim24.clients;

import com.bookingapptim24.models.PendingReview;
import com.bookingapptim24.models.reviews.AccommodationReviewWhole;
import com.bookingapptim24.models.reviews.AccommodationReviews;
import com.bookingapptim24.models.reviews.NewReview;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationReviewService {
    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("accommodation-reviews/pending")
    Call<ArrayList<PendingReview>> getAllPending();

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("accommodation-reviews/approve/{id}")
    Call<ResponseBody> approve(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("accommodation-reviews/deny/{id}")
    Call<ResponseBody> deny(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("accommodation-reviews/{accommodationId}")
    Call<AccommodationReviews> getReviewsForAccommodation(
            @Path("accommodationId") Long accommodationId,
            @Query("guestId") Long guestId
    );

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @POST("accommodation-reviews")
    Call<AccommodationReviewWhole> createAccommodationReview(@Body NewReview dto);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @DELETE("accommodation-reviews/{id}")
    Call<ResponseBody> deleteAccommodationReview(@Path("id") Long id);

}
