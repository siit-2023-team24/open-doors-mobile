package com.bookingapptim24.clients;

import com.bookingapptim24.models.NotificationDTO;
import com.bookingapptim24.models.enums.NotificationType;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("users/{id}/notifications")
    Call<List<NotificationDTO>> getAllFor(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("users/{id}/disabled-notifications")
    Call<List<NotificationType>> getDisabledFor(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @PUT("users/{id}/disabled-notifications")
    Call<ResponseBody> setDisabledFor(@Path("id") Long id, @Body List<NotificationType> types);
}
