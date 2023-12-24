package com.bookingapptim24.tools;

import com.bookingapptim24.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    //EXAMPLE: http://192.168.43.73:8080/api/
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":9090/open-doors/";

    /*
     * Ovo ce nam sluziti za debug, da vidimo da li zahtevi i odgovori idu
     * odnosno dolaze i kako izgledaju.
     * */
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        return client;
    }

    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
    public static AuthService authService = retrofit.create(AuthService.class);

}
