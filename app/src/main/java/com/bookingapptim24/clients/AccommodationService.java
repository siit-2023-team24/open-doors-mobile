package com.bookingapptim24.clients;

import com.bookingapptim24.models.AccommodationHost;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.models.AccommodationWithTotalPrice;
import com.bookingapptim24.models.SearchAndFilterAccommodations;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/all")
    Call<ArrayList<AccommodationSearchDTO>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/{id}")
    Call<AccommodationWithTotalPrice> getAccommodation(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/search")
    Call<ArrayList<AccommodationSearchDTO>> searchAccommodations(@Body SearchAndFilterAccommodations searchAndFilterDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/accommodationTypes")
    Call<ArrayList<String>> getAccommodationTypes();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/amenities")
    Call<ArrayList<String>> getAmenities();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/host/{hostId}")
    Call<ArrayList<AccommodationHost>> getForHost(@Path("hostId") Long hostId);

}
