package com.bookingapptim24.clients;

import com.bookingapptim24.models.AccommodationIdReport;
import com.bookingapptim24.models.DateRangeReport;
import com.bookingapptim24.models.DateRangeReportParams;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FinancialReportService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("financialReport/dateRangeReport")
    Call<ArrayList<DateRangeReport>> getDateRangeReport(@Body DateRangeReportParams dateRangeReportParams);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("financialReport/accommodationIdReport/{accommodationId}")
    Call<ArrayList<AccommodationIdReport>> getAccommodationIdReport(@Path("accommodationId") Long accommodationId);
}
