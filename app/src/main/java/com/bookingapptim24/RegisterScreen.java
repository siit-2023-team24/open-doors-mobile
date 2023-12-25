package com.bookingapptim24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.databinding.ActivityRegisterScreenBinding;
import com.bookingapptim24.models.UserAccount;
import com.bookingapptim24.models.enums.Country;
import com.bookingapptim24.clients.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterScreenBinding binding = ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RadioButton radioOption1 = binding.radioOption1;
        RadioButton radioOption2 = binding.radioOption2;

        radioOption1.setChecked(true);
        radioOption1.setTag("ROLE_GUEST");
        radioOption2.setTag("ROLE_HOST");

        binding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
            startActivity(intent);
            finish();
        });

        NumberPicker numberPicker = binding.number;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000000);

        binding.signUpButton.setOnClickListener(v -> {

            boolean valid = true;

            RadioGroup radioGroup = binding.role;
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String role = selectedRadioButton.getTag().toString();

            EditText editTextEmail = binding.username;
            String username = editTextEmail.getText().toString().trim();

            if (username.isEmpty() || !username.matches("[a-zA-Z0-9._+-]+@[a-z]+\\.+[a-z]+")) {
                editTextEmail.setError("Please enter a valid email address.");
                valid = false;
            }

            EditText editTextPassword = binding.password;
            String password = editTextPassword.getText().toString().trim();
            if (password.isEmpty() || password.length()<8){
                editTextPassword.setError("Password has to be at least 8 characters long.");
                valid = false;
            }

            EditText editTextConfirmPassword = binding.confirmPassword;
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();
            if (!confirmPassword.equals(password)){
                editTextConfirmPassword.setError("Passwords do not match.");
                valid = false;
            }

            EditText editTextFirstName = binding.firstName;
            String firstName = editTextFirstName.getText().toString().trim();
            if (firstName.isEmpty()){
                editTextFirstName.setError("First name is required.");
                valid=false;
            }

            EditText editTextLastName = binding.lastName;
            String lastName = editTextLastName.getText().toString().trim();
            if (lastName.isEmpty()) {
                editTextFirstName.setError("Last name is required.");
                valid=false;
            }

            EditText editTextCity = binding.city;
            String city = editTextCity.getText().toString().trim();
            if (city.isEmpty()) {
                editTextCity.setError("City is required.");
                valid=false;
            }

            EditText editTextStreet = binding.street;
            String street = editTextStreet.getText().toString().trim();
            if (street.isEmpty()) {
                editTextStreet.setError("Street is required");
                valid=false;
            }


            int number = numberPicker.getValue();

            EditText editTextPhone = binding.phone;
            String phone = editTextPhone.getText().toString().trim().replaceAll("[^0-9]", "");
            if (phone.isEmpty() || phone.length()!=10) {
                editTextPhone.setError("Please enter a valid phone with exactly 10 digits.");
                valid = false;
            }

            if(!valid) return;

            Spinner spinnerCountry = binding.countrySpinner;
            String country = spinnerCountry.getSelectedItem().toString();

            UserAccount user = new UserAccount(null, firstName, lastName, phone, street, number,
                    city, country, null, username, password, role);

            AuthService service = ClientUtils.authService;

            Call<UserAccount> call = service.register(user);
            call.enqueue(new Callback<UserAccount>() {
                @Override
                public void onResponse(Call<UserAccount> call, Response<UserAccount> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterScreen.this, "Please check your email to verify your account.", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterScreen.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<UserAccount> call, Throwable t) {
                    // Handle network failure or exception
                }
            });
        });


        String[] countryArray = new String[Country.values().length];
        for (int i = 0; i < Country.values().length; i++) {
            countryArray[i] = Country.values()[i].getCountryName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                countryArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countrySpinner.setAdapter(adapter);
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