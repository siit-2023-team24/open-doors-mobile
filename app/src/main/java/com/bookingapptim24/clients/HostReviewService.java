package com.bookingapptim24.clients;

import com.bookingapptim24.models.PendingReview;
import com.bookingapptim24.models.ReportedReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface HostReviewService {

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("host-reviews/reported")
    Call<ArrayList<ReportedReview>> getAllReported();
}
