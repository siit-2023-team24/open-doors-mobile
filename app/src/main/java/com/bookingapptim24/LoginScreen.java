package com.bookingapptim24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.Account;
import com.bookingapptim24.models.NotificationDTO;
import com.bookingapptim24.models.UserTokenState;
import com.bookingapptim24.clients.AuthService;
import com.bookingapptim24.databinding.ActivityLoginScreenBinding;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.enums.NotificationType;
import com.bookingapptim24.util.SocketService;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());

        ActivityLoginScreenBinding binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.loginButton.setOnClickListener((v) -> {
//            //temporary solution without server
//            HomeScreen.user = binding.editTextEmail.getText().toString();
//            //
//            Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
//            startActivity(intent);
//            finish();
//        });

        binding.loginButton.setOnClickListener((v) -> {

            String username = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            AuthService service = ClientUtils.authService;
            Account account = new Account(username, password);

            Call<UserTokenState> call = service.login(account);
            call.enqueue(new Callback<UserTokenState>() {
                @Override
                public void onResponse(Call<UserTokenState> call, Response<UserTokenState> response) {
                    if (response.isSuccessful()) {
                        UserTokenState userTokenState = response.body();
                        sessionManager.saveUserSession(userTokenState.getAccessToken());

                        SocketService.connect();
                        SocketService.setSessionManager(sessionManager);
                        NotificationDTO notificationDTO = new NotificationDTO(null, new Timestamp(System.currentTimeMillis()),
                                sessionManager.getUsername(), "NOTIF. FROM MOBILE", NotificationType.NEW_RESERVATION_REQUEST);
                        SocketService.sendNotification(notificationDTO);

                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("BUGBUGBUG", "BUGBUGBUG");
                        Toast.makeText(LoginScreen.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<UserTokenState> call, Throwable t) {
                    Log.d("FAILFAILFAIL", "FAILFAILFAIL");

                    Toast.makeText(LoginScreen.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                }
            });


        });

        binding.registerButton.setOnClickListener((v) -> {
            Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
            startActivity(intent);
            finish();
        });

        binding.continueButton.setOnClickListener((v) -> {
            Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
            startActivity(intent);
            finish();
        });
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
    protected void onDestroy(){
        super.onDestroy();
    }
}