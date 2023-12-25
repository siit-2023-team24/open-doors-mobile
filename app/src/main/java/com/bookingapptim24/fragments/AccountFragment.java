package com.bookingapptim24.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bookingapptim24.LoginScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.activities.EditAccountActivity;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.UserAccountView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

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
    }

    @Override
    public void onResume() {
        super.onResume();

        //todo get id
        Long userId = 1L;

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
                    image.setImageResource(R.drawable.account);

                } else {
                    Log.d("OpenDoors","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserAccountView> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });


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

        Button signOutBtn = view.findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener((v) -> {
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
                        Toast.makeText(requireActivity(), "Deleted account", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                        signOutBtn.performClick();
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button editBtn = view.findViewById(R.id.edit_account_btn);
        editBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), EditAccountActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void onDeleteAccount() {
    }
}