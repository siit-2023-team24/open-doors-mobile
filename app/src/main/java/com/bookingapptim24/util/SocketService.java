package com.bookingapptim24.util;

import android.util.Log;

import com.bookingapptim24.BuildConfig;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.NotificationDTO;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class SocketService {

    public static final String WEB_SOCKET_URL = "ws://"+ BuildConfig.IP_ADDR +":9090/open-doors/socket";
    private static WebSocketClient webSocketClient;
    private static SessionManager sessionManager;

    public static void  setSessionManager(SessionManager manager) {
        sessionManager = manager;
    }

    public static void connect() {
        URI uri = URI.create(WEB_SOCKET_URL);
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("WebSocket", "Connection opened");
                subscribeToTopics();
            }

            @Override
            public void onMessage(String message) {
                Log.i("WebSocket", "Message received on socket");
                handleWebSocketMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("WebSocket", "Connection closed: Code " + code + ", Reason: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.e("WebSocket", "Connection error", ex);
            }
        };
        webSocketClient.connect();
    }


    private static void subscribeToTopics() {
        // Subscribe to a general topic
        sendMessage("{\"subscribe\":\"/socket-publisher\"}");

        // If a user is logged in, subscribe to their specific topic
        if (sessionManager.isLoggedIn()) {
            sendMessage("{\"subscribe\":\"/socket-publisher/" + sessionManager.getUsername() + "\"}");
        }
    }

    public static void disconnect() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }
    }

    public static void sendNotification(NotificationDTO notification) {
        Gson gson = new Gson();
        String notificationJson = gson.toJson(notification);
        sendMessage(notificationJson);
    }

    private static void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
        }
    }

    private static void handleWebSocketMessage(String message) {
        // Implement your message handling logic here
        Log.i("WebSocket", "Received message: " + message);
    }
}

