package com.bookingapptim24.clients;

import com.bookingapptim24.models.PendingReview;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

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

}
