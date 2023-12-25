package com.bookingapptim24.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.LoginScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.RegisterScreen;
import com.bookingapptim24.clients.AuthService;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.PendingAccommodationService;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.ActivityCreateAccommodationBinding;
import com.bookingapptim24.models.PendingAccommodationWhole;
import com.bookingapptim24.models.UserAccount;
import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;
import com.bookingapptim24.models.enums.Country;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccommodationActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private List<String> selectedAmenities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedAmenities = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        ActivityCreateAccommodationBinding binding = ActivityCreateAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RadioButton isAutomaticFalse = binding.automaticFalse;
        RadioButton isAutomaticTrue = binding.automaticTrue;

        isAutomaticTrue.setChecked(true);
        isAutomaticTrue.setTag(true);
        isAutomaticFalse.setTag(false);

        RadioButton isPricePerGuestTrue = binding.pricePerGuestTrue;
        RadioButton isPricePerGuestFalse = binding.pricePerGuestFalse;

        isPricePerGuestTrue.setChecked(true);
        isPricePerGuestTrue.setTag(true);
        isPricePerGuestFalse.setTag(false);

        binding.backButton.setOnClickListener(v -> {
            finish();
        });

        NumberPicker deadlinePicker = binding.deadline;
        deadlinePicker.setMinValue(1);
        deadlinePicker.setMaxValue(100);

        NumberPicker minGuestsPicker = binding.minGuests;
        minGuestsPicker.setMinValue(1);
        minGuestsPicker.setMaxValue(100);

        NumberPicker maxGuestsPicker = binding.maxGuests;
        maxGuestsPicker.setMinValue(1);
        maxGuestsPicker.setMaxValue(100);

        NumberPicker numberPicker = binding.number;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000000);


        String[] countryArray = new String[Country.values().length];
        for (int i = 0; i < Country.values().length; i++) {
            countryArray[i] = Country.values()[i].getCountryName();
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                countryArray);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countrySpinner.setAdapter(countryAdapter);

        String[] typeArray = new String[AccommodationType.values().length];
        for (int i=0; i < AccommodationType.values().length; i++){
            typeArray[i] = AccommodationType.values()[i].getValue();
        }
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                typeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeSpinner.setAdapter(typeAdapter);

        String[] amenityArray = new String[Amenity.values().length];
        for (int i = 0; i < Amenity.values().length; i++) {
            amenityArray[i] = Amenity.values()[i].getAmenityName();
        }


        for (String amenity : amenityArray) {
            System.out.println(amenity);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(amenity);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedAmenities.add(amenity);
                } else {
                    selectedAmenities.remove(amenity);
                }
            });
            binding.gridLayout.addView(checkBox);
        }

        binding.createButton.setOnClickListener(v -> {
            boolean valid = true;

            String name = binding.name.getText().toString();
            if (name.isEmpty()) {
                binding.name.setError("Accommodation name is required.");
                valid = false;
            }

            String description = binding.description.getText().toString();


            int minGuests = minGuestsPicker.getValue();
            int maxGuests = maxGuestsPicker.getValue();
            if (maxGuests < minGuests) {
                maxGuests = minGuests;
            }

            Spinner spinnerType = binding.typeSpinner;
            String type = spinnerType.getSelectedItem().toString();

            EditText priceText = binding.defaultPrice;
            double price = 0;
            try {
                price = Double.parseDouble(priceText.getText().toString());
                if (price < 0) {
                    valid = false;
                    priceText.setError("Please enter a valid non-negative price.");
                }
            }
            catch (Exception e) {
                valid = false;
                priceText.setError("Please enter a valid non-negative price.");
            }

            RadioGroup pricePerGuest = binding.isPricePerGuest;
            int selectedPriceOption = pricePerGuest.getCheckedRadioButtonId();
            RadioButton selectedPriceButton = findViewById(selectedPriceOption);
            boolean isPricePerGuest = (boolean)selectedPriceButton.getTag();

            Spinner spinnerCountry = binding.countrySpinner;
            String country = spinnerCountry.getSelectedItem().toString();

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

            int deadline = deadlinePicker.getValue();

            RadioGroup automatic = binding.isAutomatic;
            int selectedAutomaticOption = automatic.getCheckedRadioButtonId();
            RadioButton selectedAutomaticButton = findViewById(selectedAutomaticOption);
            boolean isAutomatic = (boolean)selectedAutomaticButton.getTag();


            if(!valid) {
                System.out.println("THERE RIGHT THERE");
                Toast.makeText(CreateAccommodationActivity.this, "Please enter the data according to the validations.", Toast.LENGTH_SHORT);
                return;
            }

            PendingAccommodationWhole dto = new PendingAccommodationWhole(
                    null,
                    null,
                    name,
                    description,
                    "",
                    selectedAmenities,
                    new ArrayList<Long>(), //images
                    minGuests,
                    maxGuests,
                    type,
                    price,
                    isPricePerGuest,
                    city,
                    country,
                    street,
                    number,
                    deadline,
                    isAutomatic,
                    sessionManager.getUsername()
            );

            PendingAccommodationService service = ClientUtils.pendingAccommodationService;

            Call<PendingAccommodationWhole> call = service.add(dto);
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Accommodation created successfuly", Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        Toast.makeText(CreateAccommodationActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    // Handle network failure or exception
                }
            });


        });

        binding.backButton.setOnClickListener(v -> {
            this.finish();
        });
    }
}