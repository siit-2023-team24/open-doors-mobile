package com.bookingapptim24.fragments.accommodation_page;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.AccommodationWithTotalPrice;
import com.bookingapptim24.models.DateRange;
import com.bookingapptim24.models.MakeReservationRequest;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MakeReservationRequestFragment extends Fragment {
    private View view;
    private SessionManager sessionManager;
    private MakeReservationRequest requestDTO;
    private AccommodationWithTotalPrice accommodation;
    private Timestamp selectedStartDate;
    private Timestamp selectedEndDate;


    public MakeReservationRequestFragment() {}

    public static MakeReservationRequestFragment newInstance() {
        MakeReservationRequestFragment fragment = new MakeReservationRequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        ArrayList<AccommodationWithTotalPrice> accommodationContainer = (ArrayList<AccommodationWithTotalPrice>) args.getSerializable("accommodationContainer");
        accommodation = accommodationContainer.get(0);

        sessionManager = new SessionManager(requireActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_make_reservation_request, container, false);

        Button makeReservationButton = view.findViewById(R.id.make_reservation_request_button);
        Button startDateButton = view.findViewById(R.id.reservationStartDateButton);
        Button endDateButton = view.findViewById(R.id.reservationEndDateButton);

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
                return accommodation.isDateAvailable(date);
            }
        };
        constraintsBuilder.setValidator(dateValidator);

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Log.d("DATEPICKER", "KLIKNUT SAM");
                    }
                }
        );

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
        makeReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservationRequest();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void makeReservationRequest() {
        MakeReservationRequest makeReservationRequest = new MakeReservationRequest();
        makeReservationRequest.setAccommodationId(accommodation.getId());
        makeReservationRequest.setGuestId(sessionManager.getUserId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(selectedStartDate != null)
            requestDTO.setStartDate(dateFormat.format(selectedStartDate));
        if(selectedEndDate != null)
            requestDTO.setEndDate(dateFormat.format(selectedEndDate));

    }
}