package com.bookingapptim24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.provider.Settings;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!isNetworkAvailable()) {
            showNoInternetDialog();
        } else {
            startLoginScreenAfterDelay();
        }

        // Register a BroadcastReceiver to listen for network changes
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable()) {
                    // Internet connection established, proceed to LoginScreen
                    startLoginScreenAfterDelay();
                }
            }
        };
        registerNetworkReceiver();
    }

    private void startLoginScreenAfterDelay() {
        int SPLASH_TIME_OUT = 2000
                * 0 //for testing purposes
                ;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
        }
        return false;
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.no_internet_connection))
                .setCancelable(false)
                .setMessage(getString(R.string.please_connect_to_the_internet))
                .setPositiveButton(getString(R.string.go_to_wi_fi), (dialogInterface, id) -> {
                    openWifiSettings();
        })
        .setNegativeButton(this.getString(R.string.exit), (dialogInterface, id) -> {
            finish();
        });

        dialog.create().show();
    }

    private void openWifiSettings() {
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(wifiSettingsIntent);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }
}