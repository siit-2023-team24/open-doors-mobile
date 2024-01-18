package com.bookingapptim24.clients;

import com.bookingapptim24.models.MakeReservationRequest;

import java.util.ArrayList;

import com.bookingapptim24.models.ReservationRequestForGuest;
import com.bookingapptim24.models.ReservationRequestForHost;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReservationRequestService {

    @Headers({

            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations/createRequest")
    Call<MakeReservationRequest> makeReservationRequest(@Body MakeReservationRequest requestDTO);

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

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @DELETE("reservations/{id}")
    Call<ResponseBody> delete(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("reservations/cancel/{id}")
    Call<ResponseBody> cancel(@Path("id") Long id);

}
