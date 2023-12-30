package com.bookingapptim24.clients;

import com.bookingapptim24.models.NewPassword;
import com.bookingapptim24.models.User;
import com.bookingapptim24.models.UserAccountView;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @GET("users/{id}")
    Call<UserAccountView> getById(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @DELETE("users/{id}")
    Call<ResponseBody> delete(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @PUT("users/new-password")
    Call<ResponseBody> changePassword(@Body NewPassword dto);

    @Headers({
            "User-Agent: Mobile-Android"
    })
    @Multipart
    @PUT("users")
    Call<ResponseBody> edit(@Part("id") String id,
                            @Part("firstName") String firstName,
                            @Part("lastName") String lastName,
                            @Part("phone") String phone,
                            @Part("street") String street,
                            @Part("number") String number,
                            @Part("city") String city,
                            @Part("country") String country,
                            @Part("imageId") String imageId,
                            @Part MultipartBody.Part file);
}
