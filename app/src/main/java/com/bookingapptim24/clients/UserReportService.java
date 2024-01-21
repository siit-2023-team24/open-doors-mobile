package com.bookingapptim24.clients;

import com.bookingapptim24.models.NewUserReport;
import com.bookingapptim24.models.UserReport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserReportService {
    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("user-reports")
    Call<ArrayList<UserReport>> getAll();

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("user-reports/resolve/{id}")
    Call<ResponseBody> resolve(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("user-reports/dismiss/{id}")
    Call<UserReport> dismiss(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("user-reports/{id}")
    Call<List<String>> getReportableUsersForUser(
            @Path("id") Long id,
            @Query("isGuestComplainant") String isGuestComplainant
    );
    @Headers({
            "User-Agent: Mobile-Android"
    })
    @POST("user-reports")
    Call<UserReport> createUserReview(@Body NewUserReport dto);
}
