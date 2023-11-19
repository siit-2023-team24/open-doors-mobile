package com.bookingapptim24.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.R;

public class EditAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(EditAccountActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();

            //how to open the profile fragment instead od home page?
        });
    }
}