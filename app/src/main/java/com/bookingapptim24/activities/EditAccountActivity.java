package com.bookingapptim24.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.ActivityEditAccountBinding;
import com.bookingapptim24.models.User;
import com.bookingapptim24.models.UserAccountView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private EditText firstName;
    private EditText lastName;
    private EditText country;
    private EditText city;
    private EditText street;
    private EditText houseNumber;
    private EditText phone;
    private ImageView image;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditAccountBinding binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firstName = binding.firstNameET;
        lastName = binding.lastNameET;
        city = binding.cityET;
        street=binding.streetET;
        houseNumber=binding.houseNumberET;
        phone=binding.phoneET;
        image=binding.editAccountImage;

        Button saveBtn = findViewById(R.id.save_account_edit_btn);
        saveBtn.setOnClickListener((v) -> {
            onSave();

            Intent intent = new Intent(EditAccountActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.countries));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editCountrySpinner.setAdapter(adapter);
        sessionManager = new SessionManager(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        Call<UserAccountView> call = ClientUtils.userService.getById(sessionManager.getUserId());
        call.enqueue(new Callback<UserAccountView>() {
            @Override
            public void onResponse(Call<UserAccountView> call, Response<UserAccountView> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    user = response.body();
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    city.setText(user.getCity());
                    street.setText(user.getStreet());
                    houseNumber.setText(Integer.toString(user.getNumber()));
                    phone.setText(user.getPhone());
                    image.setImageResource(R.drawable.account);

                } else {
                    Log.d("OpenDoors", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserAccountView> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void onSave() {
        user.setId(sessionManager.getUserId());
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setCity(city.getText().toString());
        user.setStreet(street.getText().toString());
        user.setNumber(Integer.parseInt(houseNumber.getText().toString()));
        user.setPhone(phone.getText().toString());

        Call<ResponseBody> call = ClientUtils.userService.edit(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(EditAccountActivity.this, "Edit successful", Toast.LENGTH_SHORT).show();
                } else{
                    Log.d("OpenDoors", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}