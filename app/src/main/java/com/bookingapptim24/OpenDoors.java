package com.bookingapptim24;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bookingapptim24.clients.ClientUtils;

public class OpenDoors extends Application {

    private static OpenDoors instance;

    public static OpenDoors getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
