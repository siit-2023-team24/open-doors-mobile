package com.bookingapptim24.fragments.reservation_list;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.SearchAndFilterReservationRequests;
import com.bookingapptim24.models.enums.Amenity;
import com.bookingapptim24.models.enums.ReservationRequestStatus;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilterRequestsFragment extends Fragment {

    private View view;
    private SearchAndFilterReservationRequests searchAndFilterDTO;
    private ArrayList<String> statuses;

    public FilterRequestsFragment() {}


    public static FilterRequestsFragment newInstance() {
        FilterRequestsFragment fragment = new FilterRequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_requests, container, false);

        getRequestStatuses();

        Button filterButton = view.findViewById(R.id.filterRequestsButton);

        Bundle args = getArguments();

        ArrayList<SearchAndFilterReservationRequests> searchAndFilters = (ArrayList<SearchAndFilterReservationRequests>) args.getSerializable("searchAndFilterDTO");
        searchAndFilterDTO = searchAndFilters.get(0);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckedStatus();
                filterRequests();
            }
        });

        return view;
    }

    private void getRequestStatuses() {
        Call<ArrayList<String>> call = ClientUtils.reservationRequestService.getReservationRequestStatuses();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()) {
                    statuses = response.body();
                    loadStatuses();
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    private void loadStatuses() {
        LinearLayout statusesLayout = view.findViewById(R.id.statusesFilterLayout);
        statusesLayout.removeAllViewsInLayout();

        RadioGroup radioGroup = new RadioGroup(requireContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        if(statuses != null) {
            for(String status : statuses) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(status);

                radioGroup.addView(radioButton);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                radioButton.setLayoutParams(layoutParams);
            }
        }

        statusesLayout.addView(radioGroup);
    }

    private void getCheckedStatus() {
        LinearLayout statusesLayout = view.findViewById(R.id.statusesFilterLayout);
        ReservationRequestStatus status = null;

        RadioGroup radioGroup = (RadioGroup) statusesLayout.getChildAt(0);

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View childView = radioGroup.getChildAt(i);

            if (childView instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) childView;
                if (radioButton.isChecked()) {
                    try {
                        status = ReservationRequestStatus.valueOf(radioButton.getText().toString());
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum constant, if needed
                        e.printStackTrace();
                    }
                }
            }
        }

        searchAndFilterDTO.setStatus(status);
    }

    private void filterRequests() {
        ArrayList<SearchAndFilterReservationRequests> searchAndFilters = new ArrayList<>();
        searchAndFilters.add(searchAndFilterDTO);

        Bundle args = new Bundle();
        args.putSerializable("searchAndFilterDTO", searchAndFilters);

        Log.d("RequestFilter", searchAndFilterDTO.toString());

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.popBackStack();
        navController.navigate(R.id.nav_reservation_requests, args);
    }
}