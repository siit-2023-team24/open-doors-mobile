package com.bookingapptim24.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccommodationActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private List<String> selectedAmenities;

    private Long id = null;
    private Long accommodationId = null;
    private PendingAccommodationWhole accommodation;

    private RadioButton isAutomaticTrue;
    private RadioButton isAutomaticFalse;
    private RadioButton isPricePerGuestTrue;
    private RadioButton isPricePerGuestFalse;
    private NumberPicker deadlinePicker;
    private NumberPicker minGuestsPicker;
    private NumberPicker maxGuestsPicker;
    private NumberPicker numberPicker;
    private Spinner countrySpinner;
    private Spinner typeSpinner;
    private EditText nameET;
    private EditText descriptionET;
    private EditText defaultPriceET;
    private EditText cityET;
    private EditText streetET;



    //todo bind amenities



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedAmenities = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        ActivityCreateAccommodationBinding binding = ActivityCreateAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameET = binding.name;
        descriptionET = binding.description;
        defaultPriceET = binding.defaultPrice;
        cityET = binding.city;
        streetET = binding.street;

        isAutomaticFalse = binding.automaticFalse;
        isAutomaticTrue = binding.automaticTrue;

        isAutomaticTrue.setChecked(true);
        isAutomaticTrue.setTag(true);
        isAutomaticFalse.setTag(false);

        isPricePerGuestTrue = binding.pricePerGuestTrue;
        isPricePerGuestFalse = binding.pricePerGuestFalse;

        isPricePerGuestTrue.setChecked(true);
        isPricePerGuestTrue.setTag(true);
        isPricePerGuestFalse.setTag(false);

        binding.backButton.setOnClickListener(v -> {
            finish();
        });

        deadlinePicker = binding.deadline;
        deadlinePicker.setMinValue(1);
        deadlinePicker.setMaxValue(100);

        minGuestsPicker = binding.minGuests;
        minGuestsPicker.setMinValue(1);
        minGuestsPicker.setMaxValue(100);

        maxGuestsPicker = binding.maxGuests;
        maxGuestsPicker.setMinValue(1);
        maxGuestsPicker.setMaxValue(100);

        numberPicker = binding.number;
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
        countrySpinner = binding.countrySpinner;
        countrySpinner.setAdapter(countryAdapter);

        String[] typeArray = new String[AccommodationType.values().length];
        for (int i=0; i < AccommodationType.values().length; i++){
            typeArray[i] = AccommodationType.values()[i].getValue();
        }
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                typeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner = binding.typeSpinner;
        typeSpinner.setAdapter(typeAdapter);

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
                System.out.println("THERE RIGHT THERE - Look at that condescending smirk, see it on every guy at work.");
                Toast.makeText(CreateAccommodationActivity.this, "Please enter the data according to the validations.", Toast.LENGTH_SHORT);
                return;
            }

            PendingAccommodationWhole dto = new PendingAccommodationWhole(
                    id,
                    accommodationId,
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("id");
            accommodationId = extras.getLong("accommodationId");
            getData();
        }
    }

    private void getData() {
        Log.d("OpenDoors", "Getting accommodation details: " + id + ", " + accommodationId);
        if (id > 0) {
            Call<PendingAccommodationWhole> call = ClientUtils.pendingAccommodationService.getById(id);
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received pending accommodation: " + id);
                        accommodation = response.body();
                        setData();
                    }
                    else Log.d("OpenDoors","Message received: " + response.code());
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
        else {
            Call<PendingAccommodationWhole> call = ClientUtils.accommodationService.getForEdit(accommodationId);
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received accommodation: " + accommodationId);
                        accommodation = response.body();
                        setData();
                    }
                    else Log.d("OpenDoors","Message received: " + response.code());
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    }

    private void setData() {
        if (accommodation.isAutomatic()) {
            isAutomaticTrue.setChecked(true);
            isAutomaticFalse.setChecked(false);
        } else {
            isAutomaticTrue.setChecked(false);
            isAutomaticFalse.setChecked(true);
        }

        if (accommodation.isPricePerGuest()) {
            isPricePerGuestTrue.setChecked(true);
            isPricePerGuestFalse.setChecked(false);
        } else {
            isPricePerGuestTrue.setChecked(false);
            isPricePerGuestFalse.setChecked(true);
        }
        deadlinePicker.setValue(accommodation.getDeadline());
        minGuestsPicker.setValue(accommodation.getMinGuests());
        maxGuestsPicker.setValue(accommodation.getMaxGuests());
        numberPicker.setValue(accommodation.getNumber());

        Country accommodationCountry = Country.fromString(accommodation.getCountry());
        countrySpinner.setSelection(Arrays.asList(Country.values()).indexOf(accommodationCountry));

        AccommodationType accommodationType = AccommodationType.fromString(accommodation.getType());
        typeSpinner.setSelection(Arrays.asList(AccommodationType.values()).indexOf(accommodationType));

        nameET.setText(accommodation.getName());
        descriptionET.setText(accommodation.getDescription());
        defaultPriceET.setText(String.valueOf(accommodation.getPrice()));
        cityET.setText(accommodation.getCity());
        streetET.setText(accommodation.getStreet());
    }
}