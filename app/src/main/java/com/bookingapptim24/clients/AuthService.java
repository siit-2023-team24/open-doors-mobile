package com.bookingapptim24.clients;

import com.bookingapptim24.models.Account;
import com.bookingapptim24.models.UserAccount;
import com.bookingapptim24.models.UserTokenState;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<UserTokenState> login(@Body Account account);

    @POST("auth/register")
    Call<UserAccount> register(@Body UserAccount userAccount);
}
