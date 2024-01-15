package com.bookingapptim24.fragments.reservation_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentReservationRequestListPageBinding;
import com.bookingapptim24.fragments.FragmentTransition;
import com.bookingapptim24.fragments.pending_accommodations.PendingAccommodationHostListFragment;


public class ReservationRequestListPageFragment extends Fragment {

    private SessionManager sessionManager;
    private FragmentReservationRequestListPageBinding binding;

    public ReservationRequestListPageFragment() {}

    public static ReservationRequestListPageFragment newInstance() {
        return new ReservationRequestListPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        binding = FragmentReservationRequestListPageBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        String role = sessionManager.getRole();
        if (role.equals("ROLE_HOST")) {
            FragmentTransition.to(ReservationRequestForHostListFragment.newInstance(), requireActivity(),
                    false, R.id.reservation_request_list);
        }
        else if (role.equals("ROLE_GUEST")) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}