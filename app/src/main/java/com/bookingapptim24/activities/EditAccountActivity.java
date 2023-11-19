package com.bookingapptim24.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.databinding.ActivityEditAccountBinding;

public class EditAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditAccountBinding binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(EditAccountActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();

            //how to open the profile fragment instead od home page?
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.countries));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countrySpinner.setAdapter(adapter);
    }
}