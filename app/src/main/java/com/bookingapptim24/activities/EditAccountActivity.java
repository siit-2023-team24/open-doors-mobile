package com.bookingapptim24.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

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

    private Bitmap bitmap = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

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
        saveBtn.setOnClickListener((v) -> onSave());

        Button delImageBtn = findViewById(R.id.delete_profile_image);
        delImageBtn.setOnClickListener((v) -> {
            user.setImageId(null);
            image.setImageResource(R.drawable.account);
        });

        Button changeImageBtn = findViewById(R.id.change_profile_image);
        changeImageBtn.setOnClickListener(v -> checkPermissions());

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
        getData();
    }


    private void getData() {
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
                    if (user.getImageId() != null && user.getImageId() > 0) {
                        loadImage();
                    } else {
                        image.setImageResource(R.drawable.account);
                    }

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

    private void loadImage() {
        Call<ResponseBody> getImage = ClientUtils.imageService.getById(user.getImageId(), true);
        getImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        File imageFile = createTempImageFile(bitmap);
                        Picasso.get().load(Uri.fromFile(imageFile)).into(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else Log.e("OpenDoors", "Error getting image");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OpenDoors", "Error loading image");
                image.setImageResource(R.drawable.account);
            }
        });
    }

    private File createTempImageFile(Bitmap bitmap) throws IOException {
        File tempDir = this.getCacheDir();
        File tempFile = File.createTempFile("image", "png", tempDir);

        OutputStream os = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();

        return tempFile;
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
                Toast.makeText(EditAccountActivity.this, "No permission to open camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImage() {
        Intent imageChooserIntent = new Intent(Intent.ACTION_PICK);
        imageChooserIntent.setType("image/*");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooserIntent = Intent.createChooser(imageChooserIntent, "SElect image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            bitmap = null;
            if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    image.setImageURI(selectedImageUri);
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(bitmap);
            }
            Toast.makeText(EditAccountActivity.this, "New image added.", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("OpenDoors", "RESULT CODE: " + resultCode);
        }
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
        Log.d("OpenDoors", "idFormData: " + idFormData);

        String countryFormData = country.getSelectedItem().toString();

        String imageIdFormData = null;
        if (user.getImageId() != null)
            imageIdFormData = user.getImageId().toString();

        MultipartBody.Part imagePart = null;
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            imagePart = MultipartBody.Part.createFormData("file", "image.png", requestBody);
        }

        Call<ResponseBody> call = ClientUtils.userService.edit(idFormData, firstNameFormData, lastNameFormData,
                phoneFormData, streetFormData, numberFormData, cityFormData, countryFormData, imageIdFormData, imagePart);
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