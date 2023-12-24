package com.bookingapptim24.fragments.pending_accommodations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
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
        return inflater.inflate(R.layout.fragment_pending_accommodation_host_page, container, false);
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