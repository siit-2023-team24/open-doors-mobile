package com.bookingapptim24.clients;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ImageService {

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("image/{id}/profile/{isProfile}")
    Call<ResponseBody> getById(@Path("id") Long id, @Path("isProfile") boolean isProfile);
}
