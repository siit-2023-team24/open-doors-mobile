package com.bookingapptim24.clients;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.models.AccommodationHost;
import com.bookingapptim24.models.PendingAccommodationHost;
import com.bookingapptim24.models.PendingAccommodationWhole;
import com.bookingapptim24.models.PendingAccommodationWholeEdited;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PendingAccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("pending-accommodations")
    Call<ArrayList<PendingAccommodationHost>> getAll();


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("pending-accommodations/host/{hostId}")
    Call<ArrayList<PendingAccommodationHost>> getForHost(@Path("hostId") Long hostId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("pending-accommodations/{id}")
    Call<PendingAccommodationWhole> getById(@Path("id") Long id);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("pending-accommodations")
    Call<ResponseBody> approve(@Body PendingAccommodationHost dto);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("pending-accommodations/deny/{id}")
    Call<ResponseBody> deny(@Path("id") Long id);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("pending-accommodations")
    Call<PendingAccommodationWholeEdited> add(@Body PendingAccommodationWholeEdited dto);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @Multipart
    @POST("pending-accommodations/{id}/images")
    Call<ResponseBody> sendImages(@Path("id") Long id,
                                  @Part List<MultipartBody.Part> images);

}
