package com.bookingapptim24.tools;

import com.bookingapptim24.model.AccountDTO;
import com.bookingapptim24.model.UserAccountDTO;
import com.bookingapptim24.model.UserTokenState;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<UserTokenState> login(@Body AccountDTO accountDTO);

    @POST("auth/register")
    Call<UserAccountDTO> register(@Body UserAccountDTO userAccountDTO);
}
