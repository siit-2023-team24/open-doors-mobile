package com.bookingapptim24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bookingapptim24.model.AccountDTO;
import com.bookingapptim24.model.UserTokenState;
import com.bookingapptim24.tools.AuthService;
import com.bookingapptim24.databinding.ActivityLoginScreenBinding;
import com.bookingapptim24.tools.SessionManager;
import com.bookingapptim24.tools.Utils;

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

            AuthService service = Utils.authService;
            AccountDTO accountDTO = new AccountDTO(username, password);

            Call<UserTokenState> call = service.loginUser(accountDTO);
            call.enqueue(new Callback<UserTokenState>() {
                @Override
                public void onResponse(Call<UserTokenState> call, Response<UserTokenState> response) {
                    if (response.isSuccessful()) {
                        UserTokenState userTokenState = response.body();
                        sessionManager.saveUserSession(userTokenState.getAccessToken());
                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginScreen.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<UserTokenState> call, Throwable t) {
                    // Handle network failure or exception
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