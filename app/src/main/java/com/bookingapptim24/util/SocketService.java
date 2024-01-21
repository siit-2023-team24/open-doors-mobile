package com.bookingapptim24.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bookingapptim24.BuildConfig;
import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.NotificationDTO;
import com.bookingapptim24.models.enums.NotificationType;
import com.google.gson.Gson;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class SocketService extends Service {

    public static final String WEB_SOCKET_URL = "ws://"+ BuildConfig.IP_ADDR +":9090/open-doors/socket/websocket";
    private StompClient mStompClient;
    private SessionManager sessionManager;


    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String ACTION_SEND_NOTIFICATION = "ACTION_SEND_NOTIFICATION";


    private static final String CHANNEL_ID = "Zero channel";
    private NotificationManager notificationManager;
    private NotificationChannel channel;
    private int notificationID = 50;


    public SocketService() {}

    @SuppressLint("CheckResult")
    public void connect() {
        this.sessionManager = new SessionManager(getApplicationContext());

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEB_SOCKET_URL);
        mStompClient.connect();

        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("WEBSOCKET", "Stomp connection opened");
                    break;
                case ERROR:
                    Log.e("WEBSOCKET", "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("WEBSOCKET", "Stomp connection closed");
                    break;
            }
        });

        mStompClient.topic("/socket-publisher").subscribe(
                topicMessage -> {
                    Log.d("WEBSOCKET", topicMessage.getPayload());
                    handleNotification(topicMessage);
                },
                error -> Log.d("WEBSOCKET", error.getMessage()));

        mStompClient.topic("/socket-publisher/" + sessionManager.getUsername()).subscribe(
                topicMessage -> {
                    Log.d("WEBSOCKET", topicMessage.getPayload());
                    handleNotification(topicMessage);
                },
                error -> Log.e("WEBSOCKET", error.getMessage()));
    }

    private void handleNotification(StompMessage topicMessage) {
        NotificationDTO notificationDTO = NotificationDTO.fromJson(topicMessage.getPayload());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(notificationDTO.getType().getTypeMessage())
                .setContentText(notificationDTO.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resultIntent = new Intent(this, HomeScreen.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationID, builder.build());
    }


    public void sendNotification(NotificationDTO notification) {
        Gson gson = new Gson();
        String notificationJson = gson.toJson(notification);
        mStompClient.send("/socket-subscriber/send/message", notificationJson).subscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = getSystemService(NotificationManager.class);
        if (intent != null)
        {
            String action = intent.getAction();
            Log.i("SERVICE STARTED", "YES");
            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_SEND_NOTIFICATION:
                    Bundle extras = intent.getExtras();
                    String username = extras.getString("username");
                    String message = extras.getString("message");
                    String type = extras.getString("type");
                    NotificationDTO notificationDTO = new NotificationDTO(null, System.currentTimeMillis(),
                            username, message, NotificationType.fromString(type));
                    sendNotification(notificationDTO);
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    onDestroy();
                    break;
            }
        }
        return START_STICKY;
    }

    private void startForegroundService()
    {
        connect();
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent(this, HomeScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Open Doors");
        bigTextStyle.bigText("Return home");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.logo);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_opaque);
        builder.setLargeIcon(largeIconBitmap);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        startForeground(notificationID, builder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Stop socket service.", Toast.LENGTH_LONG).show();
        stopForeground(true);  // Stop foreground service and remove the notification.
        stopSelf();
    }
}

