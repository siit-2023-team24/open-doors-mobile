package com.bookingapptim24.clients;

import com.bookingapptim24.models.PendingReview;
import com.bookingapptim24.models.ReportedReview;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HostReviewService {

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("host-reviews/reported")
    Call<ArrayList<ReportedReview>> getAllReported();

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @DELETE("host-reviews/{id}")
    Call<ResponseBody> delete(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @POST("host-reviews/{id}/status")
    Call<ResponseBody> dismiss(@Path("id") Long id);
}
