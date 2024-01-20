package com.bookingapptim24.fragments.home_page;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.models.SearchAndFilterAccommodations;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAccommodationsFragment extends Fragment {
    private View view;
    private SearchAndFilterAccommodations searchAndFilterDTO;
    private Timestamp selectedStartDate;
    private Timestamp selectedEndDate;

    public SearchAccommodationsFragment() {}

    public static SearchAccommodationsFragment newInstance() {
        SearchAccommodationsFragment fragment = new SearchAccommodationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_accommodations, container, false);

        Button startDateButton = view.findViewById(R.id.startDateButton);
        Button endDateButton = view.findViewById(R.id.endDateButton);
        Button searchButton = view.findViewById(R.id.searchButton);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        long timestamp = selectedDate.getTimeInMillis();
                        selectedStartDate = new Timestamp(timestamp);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                if(selectedEndDate != null)
                    datePickerDialog.getDatePicker().setMaxDate(selectedEndDate.getTime());

                datePickerDialog.show();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        long timestamp = selectedDate.getTimeInMillis();
                        selectedEndDate = new Timestamp(timestamp);
                    }
                }, year, month, day);
                if(selectedStartDate != null)
                    datePickerDialog.getDatePicker().setMinDate(selectedStartDate.getTime());
                else
                    datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());

                datePickerDialog.show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText location = view.findViewById(R.id.locationEditText);
                EditText numOfGuests = view.findViewById(R.id.numberOfGuestsEditText);
                String locationText = location.getText().toString().trim();
                String numOfGuestsText = numOfGuests.getText().toString().trim();

                if(!locationText.isEmpty())
                    searchAndFilterDTO.setLocation(locationText.trim());
                if (!numOfGuestsText.isEmpty()) {
                    try {
                        int numOfGuestsValue = Integer.parseInt(numOfGuestsText);
                        searchAndFilterDTO.setGuestNumber(numOfGuestsValue);
                    } catch (NumberFormatException e) {
                        // Handle the case where numOfGuestsText is not a valid integer
                        e.printStackTrace();
                    }
                }

                searchAccommodations();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        ArrayList<SearchAndFilterAccommodations> searchAndFilters = (ArrayList<SearchAndFilterAccommodations>) args.getSerializable("searchAndFilterDTO");
        searchAndFilterDTO = searchAndFilters.get(0);
        Log.d("sfDTO", searchAndFilterDTO.toString());
        if(searchAndFilterDTO.getLocation() != null) {
            EditText location = view.findViewById(R.id.locationEditText);
            location.setText(searchAndFilterDTO.getLocation());
        }
        if(searchAndFilterDTO.getGuestNumber() != null) {
            EditText numOfGuests = view.findViewById(R.id.numberOfGuestsEditText);
            numOfGuests.setText(searchAndFilterDTO.getGuestNumber().toString());
        }
        searchAndFilterDTO.setStartDate(null);
        searchAndFilterDTO.setEndDate(null);
    }

    @Override
    public void onPause() {
        super.onPause();

        EditText location = view.findViewById(R.id.locationEditText);
        EditText numOfGuests = view.findViewById(R.id.numberOfGuestsEditText);
        String locationText = location.getText().toString().trim();
        String numOfGuestsText = numOfGuests.getText().toString().trim();

        if(!locationText.isEmpty())
            searchAndFilterDTO.setLocation(locationText);
        else
            searchAndFilterDTO.setLocation(null);

        if (!numOfGuestsText.isEmpty()) {
            try {
                int numOfGuestsValue = Integer.parseInt(numOfGuestsText);
                searchAndFilterDTO.setGuestNumber(numOfGuestsValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            searchAndFilterDTO.setGuestNumber(null);
        }
    }


    private void searchAccommodations() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(selectedStartDate != null)
            searchAndFilterDTO.setStartDate(dateFormat.format(selectedStartDate));
        if(selectedEndDate != null)
            searchAndFilterDTO.setEndDate(dateFormat.format(selectedEndDate));

        EditText location = view.findViewById(R.id.locationEditText);
        EditText numOfGuests = view.findViewById(R.id.numberOfGuestsEditText);
        String locationText = location.getText().toString().trim();
        String numOfGuestsText = numOfGuests.getText().toString().trim();

        if(!locationText.isEmpty())
            searchAndFilterDTO.setLocation(locationText);
        else
            searchAndFilterDTO.setLocation(null);

        if (!numOfGuestsText.isEmpty()) {
            try {
                int numOfGuestsValue = Integer.parseInt(numOfGuestsText);
                searchAndFilterDTO.setGuestNumber(numOfGuestsValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            searchAndFilterDTO.setGuestNumber(null);
        }

        Log.d("FilterAccommodations", searchAndFilterDTO.toString());

        ArrayList<SearchAndFilterAccommodations> searchAndFilters = new ArrayList<>();
        searchAndFilters.add(searchAndFilterDTO);

        Bundle args = new Bundle();
        args.putSerializable("searchAndFilterDTO", searchAndFilters);

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.popBackStack();
        navController.navigate(R.id.nav_show_all, args);
    }
}