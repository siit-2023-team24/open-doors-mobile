package com.bookingapptim24.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bookingapptim24.additionalAdapters.BitmapAdapter;
import com.bookingapptim24.additionalAdapters.ImageAdapter;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.PendingAccommodationService;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.ActivityCreateAccommodationBinding;
import com.bookingapptim24.models.PendingAccommodationWhole;
import com.bookingapptim24.models.PendingAccommodationWholeEdited;
import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;
import com.bookingapptim24.models.enums.Country;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccommodationActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private Long id = null;
    private Long accommodationId = null;
    private PendingAccommodationWhole accommodation;


    private List<String> selectedAmenities;
    private List<Long> oldImages = new ArrayList<>();
    private List<Long> toDeleteImages = new ArrayList<>();
    private List<Bitmap> newImages = new ArrayList<>();

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
    private ViewPager2 viewPager;
    private ViewPager2 viewPagerNewImages;


    //todo bind amenities


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;


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

        viewPager = binding.viewPager;
        viewPagerNewImages = binding.viewPagerNewImages;


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

            if (id != null && id == 0) id = null;
            if (accommodationId != null && accommodationId == 0) accommodationId = null;

            PendingAccommodationWholeEdited dto = new PendingAccommodationWholeEdited(
                    id,
                    accommodationId,
                    name,
                    description,
                    "",
                    selectedAmenities,
                    oldImages,
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
                    sessionManager.getUsername(),
                    toDeleteImages
            );

            PendingAccommodationService service = ClientUtils.pendingAccommodationService;

            Call<PendingAccommodationWholeEdited> call = service.add(dto);
            call.enqueue(new Callback<PendingAccommodationWholeEdited>() {
                @Override
                public void onResponse(Call<PendingAccommodationWholeEdited> call, Response<PendingAccommodationWholeEdited> response) {
                    if (response.isSuccessful()) {
                        sendImages(response.body().getId());
                        Toast.makeText(getApplicationContext(), "Accommodation created successfully", Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        Toast.makeText(CreateAccommodationActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PendingAccommodationWholeEdited> call, Throwable t) {
                    // Handle network failure or exception
                }
            });


        });

        binding.backButton.setOnClickListener(v -> this.finish());

        binding.selectImageButton.setOnClickListener(v -> checkPermissions());

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
                        if (accommodation.getImages() != null)
                            oldImages = accommodation.getImages();
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
                        if (accommodation.getImages() != null)
                            oldImages = accommodation.getImages();
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

        //todo set amenities, availabilities and rates

        if (accommodation.getImages() != null && accommodation.getImages().size() > 0) {
            viewPager.setAdapter(new ImageAdapter(getApplicationContext(), accommodation.getImages()));
        }

    }


    public void deleteImages(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to delete all images for this accommodation?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, id) -> {
                    toDeleteImages = oldImages;
                    newImages.clear();
                    viewPager.setAdapter(new ImageAdapter(getApplicationContext(), new ArrayList<>()));
                    Log.d("OpenDoors", "Images set to delete.");
                    Toast.makeText(this, "Images set to be deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
        dialog.create().show();
    }

    private void checkPermissions() {
        Log.d("OpenDoors", "Checking permissions.");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "No permission to open camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImage() {
        Intent imageChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageChooserIntent.setType("image/*");

        imageChooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooserIntent = Intent.createChooser(imageChooserIntent, "Select image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            if (data.getClipData() != null) {
                int countSelected = data.getClipData().getItemCount();
                for (int i=0; i < countSelected; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        newImages.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                newImages.add(bitmap);
            }
            Toast.makeText(this, "New image added.", Toast.LENGTH_SHORT).show();
            viewPagerNewImages.setAdapter(new BitmapAdapter(getApplicationContext(), newImages));
        }
        else {
            Log.e("OpenDoors", "RESULT CODE: " + resultCode);
        }
    }

    private void sendImages(Long pendingId) {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < newImages.size(); i++) {
            Bitmap bitmap = newImages.get(i);
            String filename = "image" + i + ".png";
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] bytes = out.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bytes);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", filename, requestBody);
            imageParts.add(imagePart);

            Call<ResponseBody> call = ClientUtils.pendingAccommodationService.sendImages(pendingId, imageParts);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(CreateAccommodationActivity.this, "Uploading images complete", Toast.LENGTH_SHORT).show();
                        Log.d("OpenDoors", "Sending accommodation images completed");
                    } else {
                        Toast.makeText(CreateAccommodationActivity.this, "Uploading images failed", Toast.LENGTH_SHORT).show();
                        Log.d("OpenDoors", "Sending accommodation images failed: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("OpenDoors", "Sending accommodation images failed");
                }
            });

        }
    }
}