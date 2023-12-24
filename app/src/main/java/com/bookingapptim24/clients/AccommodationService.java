package com.bookingapptim24.clients;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.models.AccommodationHost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/all")
    Call<ArrayList<Accommodation>> getAll();


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/host/{hostId}")
    Call<ArrayList<AccommodationHost>> getForHost(@Path("hostId") Long hostId);




}
