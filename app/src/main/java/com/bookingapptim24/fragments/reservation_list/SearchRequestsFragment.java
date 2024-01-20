package com.bookingapptim24.fragments.reservation_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.models.SearchAndFilterAccommodations;
import com.bookingapptim24.models.SearchAndFilterReservationRequests;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchRequestsFragment extends Fragment {

    private View view;
    private SearchAndFilterReservationRequests searchAndFilterDTO;
    private Timestamp selectedStartDate;
    private Timestamp selectedEndDate;

    public SearchRequestsFragment() {}


    public static SearchRequestsFragment newInstance() {
        SearchRequestsFragment fragment = new SearchRequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_requests, container, false);

        Button searchButton = view.findViewById(R.id.searchRequestsButton);

        makeStartDatePicker();
        makeEndDatePicker();

        Bundle args = getArguments();

        ArrayList<SearchAndFilterReservationRequests> searchAndFilters = (ArrayList<SearchAndFilterReservationRequests>) args.getSerializable("searchAndFilterDTO");
        searchAndFilterDTO = searchAndFilters.get(0);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRequests();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        ArrayList<SearchAndFilterReservationRequests> searchAndFilters = (ArrayList<SearchAndFilterReservationRequests>) args.getSerializable("searchAndFilterDTO");
        searchAndFilterDTO = searchAndFilters.get(0);
        Log.d("sfDTO", searchAndFilterDTO.toString());
        if(searchAndFilterDTO.getAccommodationName() != null) {
            EditText accommodationName = view.findViewById(R.id.accommodationNameEditText);
            accommodationName.setText(searchAndFilterDTO.getAccommodationName());
        }
        searchAndFilterDTO.setStartDate(null);
        searchAndFilterDTO.setEndDate(null);
    }

    @Override
    public void onPause() {
        super.onPause();

        EditText accommodationNameEditText = view.findViewById(R.id.accommodationNameEditText);
        String accommodationName = accommodationNameEditText.getText().toString().trim();
        if(!accommodationName.isEmpty()) {
            searchAndFilterDTO.setAccommodationName(accommodationName);
        }
    }

    private void searchRequests() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(selectedStartDate != null)
            searchAndFilterDTO.setStartDate(dateFormat.format(selectedStartDate));
        if(selectedEndDate != null)
            searchAndFilterDTO.setEndDate(dateFormat.format(selectedEndDate));

        EditText accommodationNameTextView = view.findViewById(R.id.accommodationNameEditText);
        String accommodationNameText = accommodationNameTextView.getText().toString().trim();
        if(!accommodationNameText.isEmpty())
            searchAndFilterDTO.setAccommodationName(accommodationNameText);

        ArrayList<SearchAndFilterReservationRequests> searchAndFilters = new ArrayList<>();
        searchAndFilters.add(searchAndFilterDTO);

        Bundle args = new Bundle();
        args.putSerializable("searchAndFilterDTO", searchAndFilters);

        Log.d("SearchAndFilter", searchAndFilterDTO.toString());

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.popBackStack();
        navController.navigate(R.id.nav_reservation_requests, args);
    }

    public void makeStartDatePicker() {
        Button startDateButton = view.findViewById(R.id.startDateRequestsButton);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }

            @Override
            public boolean isValid(long date) {
                long currentDate = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                if(selectedEndDate != null) {
                    if(date > selectedEndDate.getTime()) return false;
                }
                if(date < currentDate)
                    return false;

                return true;
            }
        };
        constraintsBuilder.setValidator(dateValidator);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A START DATE");
        materialDateBuilder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        selectedStartDate = new Timestamp(selection);
                    }
                }
        );
    }

    public void makeEndDatePicker() {
        Button endDateButton = view.findViewById(R.id.endDateRequestsButton);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }

            @Override
            public boolean isValid(long date) {
                long currentDate = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                if(selectedStartDate != null) {
                    if(date < selectedStartDate.getTime()) return false;
                }
                if(date < currentDate)
                    return false;

                return true;
            }
        };
        constraintsBuilder.setValidator(dateValidator);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT AN END DATE");
        materialDateBuilder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        selectedEndDate = new Timestamp(selection);
                    }
                }
        );
    }
}