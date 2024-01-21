package com.bookingapptim24.clients;


import com.bookingapptim24.BuildConfig;
import com.bookingapptim24.OpenDoors;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR +":9090/open-doors/";

    public static OkHttpClient test() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        TokenInterceptor tokenInterceptor = new TokenInterceptor(new SessionManager(OpenDoors.getContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .client(test())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
            .build();
    public static AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public static AuthService authService = retrofit.create(AuthService.class);
    public static PendingAccommodationService pendingAccommodationService = retrofit.create(PendingAccommodationService.class);
    public static UserService userService = retrofit.create(UserService.class);
    public static ImageService imageService = retrofit.create(ImageService.class);
    public static AccommodationReviewService accommodationReviewService = retrofit.create(AccommodationReviewService.class);
    public static HostReviewService hostReviewService = retrofit.create(HostReviewService.class);
    public static UserReportService userReportService = retrofit.create(UserReportService.class);
    public static ReservationRequestService reservationRequestService = retrofit.create(ReservationRequestService.class);
    public static NotificationService notificationService = retrofit.create(NotificationService.class);
}
