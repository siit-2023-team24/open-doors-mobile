package com.bookingapptim24.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
            requireActivity().finish();
        });

        Button delBtn = view.findViewById(R.id.del_btn);
        delBtn.setOnClickListener((v) -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
            dialog.setMessage("Are you sure you want to delete your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        Toast.makeText(requireActivity(), "Deleted account", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                        signOutBtn.performClick();
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button editBtn = view.findViewById(R.id.edit_btn);
        editBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), EditAccountActivity.class);
            startActivity(intent);
        });

        return view;
    }
}