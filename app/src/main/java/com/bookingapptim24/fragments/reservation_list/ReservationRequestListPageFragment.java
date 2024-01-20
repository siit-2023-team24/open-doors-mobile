package com.bookingapptim24.fragments.reservation_list;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentReservationRequestListPageBinding;
import com.bookingapptim24.fragments.FragmentTransition;
import com.bookingapptim24.fragments.pending_accommodations.PendingAccommodationHostListFragment;
import com.bookingapptim24.models.ReservationRequestForGuest;
import com.bookingapptim24.models.ReservationRequestForHost;
import com.bookingapptim24.models.SearchAndFilterAccommodations;
import com.bookingapptim24.models.SearchAndFilterReservationRequests;

import java.util.ArrayList;


public class ReservationRequestListPageFragment extends Fragment {
    private View view;
    private SessionManager sessionManager;
    private FragmentReservationRequestListPageBinding binding;
    private SearchAndFilterReservationRequests searchAndFilterDTO;

    public ReservationRequestListPageFragment() {}

    public static ReservationRequestListPageFragment newInstance() {
        return new ReservationRequestListPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        binding = FragmentReservationRequestListPageBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        if(getArguments() != null) {
            ArrayList<SearchAndFilterReservationRequests> searchAndFilters = (ArrayList<SearchAndFilterReservationRequests>) getArguments().getSerializable("searchAndFilterDTO");
            searchAndFilterDTO = searchAndFilters.get(0);
        }

        if(searchAndFilterDTO == null)
            searchAndFilterDTO = new SearchAndFilterReservationRequests();

        ArrayList<SearchAndFilterReservationRequests> searchAndFilters = new ArrayList<>();
        searchAndFilters.add(searchAndFilterDTO);
        Bundle args = new Bundle();
        args.putSerializable("searchAndFilterDTO", searchAndFilters);

        Button searchButton = view.findViewById(R.id.searchRequestsButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.nav_fragment_search_reservation_requests, args);
            }
        });

        Button filterButton = view.findViewById(R.id.filterRequestsButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.nav_fragment_filter_reservation_requests, args);
            }
        });

        String role = sessionManager.getRole();
        if (role.equals("ROLE_HOST")) {
            FragmentTransition.to(new ReservationRequestForHostListFragment(searchAndFilterDTO), requireActivity(),
                    false, R.id.reservation_request_list);
        }
        else if (role.equals("ROLE_GUEST")) {
            FragmentTransition.to(new ReservationRequestForGuestListFragment(searchAndFilterDTO), requireActivity(),
                    false, R.id.reservation_request_list);
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}