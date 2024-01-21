package com.bookingapptim24.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bookingapptim24.BuildConfig;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.NotificationDTO;
import com.bookingapptim24.models.enums.NotificationType;
import com.google.gson.Gson;

import java.sql.Timestamp;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class SocketService {

    public static final String WEB_SOCKET_URL = "ws://"+ BuildConfig.IP_ADDR +":9090/open-doors/socket/websocket";
    private StompClient mStompClient;
    private SessionManager sessionManager;


    public SocketService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    public void  setSessionManager(SessionManager manager) {
        sessionManager = manager;
    }


    @SuppressLint("CheckResult")
    public void connect() {
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
                topicMessage -> Log.d("WEBSOCKET", topicMessage.getPayload()),
                error -> Log.d("WEBSOCKET", error.getMessage()));

        mStompClient.topic("/socket-publisher/" + sessionManager.getUsername()).subscribe(
                topicMessage -> Log.d("WEBSOCKET", topicMessage.getPayload()),
                error -> Log.e("WEBSOCKET", error.getMessage()));


        NotificationDTO notificationDTO = new NotificationDTO(null, System.currentTimeMillis(),
                sessionManager.getUsername(), "NOTIFICATION FROM MOBILE", NotificationType.NEW_RESERVATION_REQUEST);

        sendNotification(notificationDTO);
    }


    public void sendNotification(NotificationDTO notification) {
        Gson gson = new Gson();
        String notificationJson = gson.toJson(notification);
        mStompClient.send("/socket-subscriber/send/message", notificationJson).subscribe();
    }

}

