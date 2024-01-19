package com.bookingapptim24.fragments.reservation_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;


public class FilterRequestsFragment extends Fragment {

    private View view;

    public FilterRequestsFragment() {
        // Required empty public constructor
    }


    public static FilterRequestsFragment newInstance() {
        FilterRequestsFragment fragment = new FilterRequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_requests, container, false);


        return view;
    }
}