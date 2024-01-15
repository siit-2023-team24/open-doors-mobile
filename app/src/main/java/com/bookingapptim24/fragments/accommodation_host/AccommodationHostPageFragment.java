package com.bookingapptim24.fragments.accommodation_host;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.bookingapptim24.R;
import com.bookingapptim24.databinding.FragmentAccommodationHostPageBinding;
import com.bookingapptim24.fragments.FragmentTransition;

public class AccommodationHostPageFragment extends Fragment {

    private FragmentAccommodationHostPageBinding binding;


    public static AccommodationHostPageFragment newInstance() {
        return new AccommodationHostPageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationHostPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.i("OpenDoors", "onCreateView AccommodationHost List Page");
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(AccommodationHostListFragment.newInstance(), getActivity(), false, R.id.accommodation_host_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}