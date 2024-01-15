package com.bookingapptim24.clients;

import com.bookingapptim24.models.ReservationRequestForGuest;
import com.bookingapptim24.models.ReservationRequestForHost;
import com.bookingapptim24.models.UserAccountView;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ReservationRequestService {

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("reservations/host/{id}")
    Call<ArrayList<ReservationRequestForHost>> getForHost(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("reservations/all/guest/{id}")
    Call<ArrayList<ReservationRequestForGuest>> getForGuest(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("reservations/confirm/{id}")
    Call<ResponseBody> confirm(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("reservations/deny/{id}")
    Call<ResponseBody> deny(@Path("id") Long id);

}
