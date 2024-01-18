package com.bookingapptim24.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bookingapptim24.LoginScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.activities.EditAccountActivity;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.UserAccountView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private SessionManager sessionManager;
    private UserAccountView user = new UserAccountView();

    private TextView email;
    private TextView firstName;
    private TextView lastName;
    private TextView country;
    private TextView city;
    private TextView street;
    private TextView houseNumber;
    private TextView phone;
    private TextView role;

    private ImageView image;
    private Button signOutBtn;


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("Open Doors", "AccountFr onCreate()");
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireActivity().getApplicationContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserData();
    }

    private void getUserData() {
        Long userId = sessionManager.getUserId();
        Call<UserAccountView> call = ClientUtils.userService.getById(userId);
        call.enqueue(new Callback<UserAccountView>() {
            @Override
            public void onResponse(Call<UserAccountView> call, Response<UserAccountView> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "User get by id");
                    user = response.body();
                    email.setText(user.getUsername());
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    country.setText(user.getCountry());
                    city.setText(user.getCity());
                    street.setText(user.getStreet());
                    houseNumber.setText(Integer.toString(user.getNumber()));
                    phone.setText(user.getPhone());
                    role.setText(user.getRole());
                    if (user.getImageId() != null && user.getImageId() > 0) {
                        loadImage();
                    } else {
                        image.setImageResource(R.drawable.account);
                    }

                } else {
                    Log.d("OpenDoors","Message received: "+response.code());
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
        File tempDir = getContext().getCacheDir();
        File tempFile = File.createTempFile("image", "png", tempDir);

        OutputStream os = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();

        return tempFile;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Open Doors", "AccountFr onCreateView()");
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        email = view.findViewById(R.id.emailTV);
        firstName = view.findViewById(R.id.firstNameTV);
        lastName = view.findViewById(R.id.lastNameTV);
        country = view.findViewById(R.id.countryTV);
        city = view.findViewById(R.id.cityTV);
        street = view.findViewById(R.id.streetTV);
        houseNumber = view.findViewById(R.id.houseNumberTV);
        phone = view.findViewById(R.id.phoneTV);
        role = view.findViewById(R.id.roleTV);
        image = view.findViewById(R.id.account_image);

        signOutBtn = view.findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener((v) -> {
            sessionManager.logout();
            Intent intent = new Intent(getActivity(), LoginScreen.class);
            startActivity(intent);
            requireActivity().finish();
        });

        Button delBtn = view.findViewById(R.id.del_account_btn);
        delBtn.setOnClickListener((v) -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
            dialog.setMessage("Are you sure you want to delete your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDeleteAccount();
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button changePasswordBtn = view.findViewById(R.id.change_password_btn);
        changePasswordBtn.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("username", sessionManager.getUsername());
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            navController.navigate(R.id.nav_edit_password, args);
            //FragmentTransition.to(new EditPasswordFragment(), requireActivity(), false, R.layout.fragment_edit_password);
        });

        Button editBtn = view.findViewById(R.id.edit_account_btn);
        editBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), EditAccountActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void onDeleteAccount() {
        Long id = sessionManager.getUserId();
        Call<ResponseBody> call = ClientUtils.userService.delete(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(requireActivity(), "Deleted account", Toast.LENGTH_SHORT).show();
                    signOutBtn.performClick();

                } else {
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