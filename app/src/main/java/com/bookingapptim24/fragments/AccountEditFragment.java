package com.bookingapptim24.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;


public class AccountEditFragment extends Fragment {

    public AccountEditFragment() {
        // Required empty public constructor
    }

    public static AccountEditFragment newInstance(String param1, String param2) {
        return new AccountEditFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_edit, container, false);
    }
}