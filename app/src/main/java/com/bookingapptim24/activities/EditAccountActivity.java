package com.bookingapptim24.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.ActivityEditAccountBinding;
import com.bookingapptim24.models.User;
import com.bookingapptim24.models.UserAccountView;
import com.bookingapptim24.models.enums.Country;

import java.io.File;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private EditText firstName;
    private EditText lastName;
    private Spinner country;
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
        country = binding.editCountrySpinner;
        city = binding.cityET;
        street=binding.streetET;
        houseNumber=binding.houseNumberET;
        phone=binding.phoneET;
        image=binding.editAccountImage;

        Button saveBtn = findViewById(R.id.save_account_edit_btn);
        saveBtn.setOnClickListener((v) -> {
            onSave();
        });

        String[] countryArray = new String[Country.values().length];
        for (int i = 0; i < Country.values().length; i++) {
            countryArray[i] = Country.values()[i].getCountryName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countryArray);

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
                    if (user == null) return;
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    city.setText(user.getCity());
                    street.setText(user.getStreet());
                    houseNumber.setText(Integer.toString(user.getNumber()));
                    phone.setText(user.getPhone());
                    Country userCountry = Country.fromString(user.getCountry());
                    country.setSelection(Arrays.asList(Country.values()).indexOf(userCountry));

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

        String idFormData = String.valueOf(sessionManager.getUserId());
        String firstNameFormData = firstName.getText().toString().trim();
        if (firstNameFormData.isEmpty()){
            firstName.setError("First name is required.");
            return;
        }
        String lastNameFormData = lastName.getText().toString().trim();
        if (lastNameFormData.isEmpty()) {
            lastName.setError("Last name is required.");
            return;
        }
        String phoneFormData = phone.getText().toString().trim();
        if (phoneFormData.isEmpty()) {
            phone.setError("Phone number is required.");
            return;
        }
        String streetFormData = street.getText().toString().trim();
        if (streetFormData.isEmpty()) {
            street.setError("Street name is required");
            return;
        }
        String numberFormData = houseNumber.getText().toString().trim();
        if (numberFormData.isEmpty()) {
            houseNumber.setError("House number is required");
            return;
        }
        try {
            user.setNumber(Integer.parseInt(numberFormData));
        } catch (Exception e) {
            houseNumber.setError("House number must be a valid number.");
        }
        String cityFormData = city.getText().toString().trim();
        if (cityFormData.isEmpty()) {
            city.setError("City is required.");
            return;
        }
        String countryFormData = country.getSelectedItem().toString();

        String imageIdFormData = null;

        File imageFile = null;
        RequestBody requestFile = null;
        MultipartBody.Part filePart = null;


        Log.d("OpenDoors", "idFormData: " + idFormData);


//        imageFile = new File("");
//        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
//        filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        Call<ResponseBody> call = ClientUtils.userService.edit(idFormData, firstNameFormData, lastNameFormData, phoneFormData, streetFormData, numberFormData, cityFormData, countryFormData, imageIdFormData, filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(EditAccountActivity.this, "Edit successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditAccountActivity.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
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