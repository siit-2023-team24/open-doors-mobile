package com.bookingapptim24.clients;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor{
    private final SessionManager sessionManager;

    public TokenInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        String accessToken = sessionManager.getToken();
        if (accessToken != null) {
            Request modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            return chain.proceed(modifiedRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
