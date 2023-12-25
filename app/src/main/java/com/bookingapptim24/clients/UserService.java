package com.bookingapptim24.clients;

import com.bookingapptim24.models.AccommodationHost;
import com.bookingapptim24.models.UserAccountView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("users/{id}")
    Call<UserAccountView> getById(@Path("id") Long id);
}
