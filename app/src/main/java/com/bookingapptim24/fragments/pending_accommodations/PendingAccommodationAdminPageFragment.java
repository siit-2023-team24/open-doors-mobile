package com.bookingapptim24.fragments.pending_accommodations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.databinding.FragmentPendingAccommodationAdminPageBinding;
import com.bookingapptim24.fragments.FragmentTransition;

public class PendingAccommodationAdminPageFragment extends Fragment {


    private FragmentPendingAccommodationAdminPageBinding binding;

    public static PendingAccommodationAdminPageFragment newInstance() {
        return new PendingAccommodationAdminPageFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_accommodation_admin_page, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(PendingAccommodationAdminListFragment.newInstance(), getActivity(), false, R.id.pending_accommodation_admin_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}