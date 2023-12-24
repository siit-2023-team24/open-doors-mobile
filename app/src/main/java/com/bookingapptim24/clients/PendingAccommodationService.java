package com.bookingapptim24.clients;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.models.AccommodationHost;
import com.bookingapptim24.models.PendingAccommodationHost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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


}
