package com.bookingapptim24.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.LoginScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.activities.EditAccountActivity;

public class AccountFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Open Doors", "AccountFr onCreateView()");
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button signOutBtn = view.findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), LoginScreen.class);
            startActivity(intent);
        });

        Button delBtn = view.findViewById(R.id.del_btn);
        delBtn.setOnClickListener((v) -> {
            //TODO
        });

        Button editBtn = view.findViewById(R.id.edit_btn);
        editBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), EditAccountActivity.class);
            startActivity(intent);
        });

        return view;
    }
}