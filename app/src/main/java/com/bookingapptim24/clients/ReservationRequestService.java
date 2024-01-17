package com.bookingapptim24.clients;

import com.bookingapptim24.models.MakeReservationRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReservationRequestService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations/createRequest")
    Call<MakeReservationRequest> makeReservationRequest(@Body MakeReservationRequest requestDTO);
}
