package com.bookingapptim24.fragments.pending_accommodations;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bookingapptim24.HomeScreen;
import com.bookingapptim24.LoginScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.activities.CreateAccommodationActivity;
import com.bookingapptim24.databinding.FragmentAccommodationHostPageBinding;
import com.bookingapptim24.databinding.FragmentPendingAccommodationHostListBinding;
import com.bookingapptim24.databinding.FragmentPendingAccommodationHostPageBinding;
import com.bookingapptim24.fragments.FragmentTransition;

public class PendingAccommodationHostPageFragment extends Fragment {

    private FragmentPendingAccommodationHostPageBinding binding;

    public static PendingAccommodationHostPageFragment newInstance() {
        return new PendingAccommodationHostPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_accommodation_host_page, container, false);
        Button createButton = view.findViewById(R.id.btnCreateAccommodation);
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CreateAccommodationActivity.class);
            startActivity(intent);
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(PendingAccommodationHostListFragment.newInstance(), getActivity(), false, R.id.pending_accommodation_host_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}