package com.bookingapptim24.fragments.accommodation_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Parcel;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.models.AccommodationSeasonalRate;
import com.bookingapptim24.models.AccommodationWithTotalPrice;
import com.bookingapptim24.models.DateRange;
import com.bookingapptim24.models.MakeReservationRequest;
import com.bookingapptim24.models.SearchAndFilterAccommodations;
import com.bookingapptim24.models.SeasonalRatesPricing;
import com.bookingapptim24.models.enums.NotificationType;
import com.bookingapptim24.util.SocketService;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeReservationRequestFragment extends Fragment {
    private View view;
    private SessionManager sessionManager;
    private AccommodationWithTotalPrice accommodation;
    private Timestamp selectedStartDate;
    private Timestamp selectedEndDate;
    private MakeReservationRequest requestDTO;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_make_reservation_request, container, false);

        Button makeReservationButton = view.findViewById(R.id.make_reservation_request_button);
        EditText numberOfGuestsEditText = view.findViewById(R.id.numberOfGuestsEditText);

        makeReservationButton.setEnabled(false);

        makeStartDatePicker();
        makeEndDatePicker();
        makeFilterForGuestNumber();

        numberOfGuestsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                boolean isInputNotEmpty = charSequence.length() > 0;
                if(!isInputNotEmpty) {
                    makeReservationButton.setEnabled(false);
                }

                try {
                    int numOfGuestsValue = Integer.parseInt(charSequence.toString());
                    requestDTO.setNumberOfGuests(numOfGuestsValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(isInputNotEmpty && selectedStartDate != null && selectedEndDate != null) {
                    makeReservationButton.setEnabled(true);
                    getPricing();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
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

        requestDTO = new MakeReservationRequest();
        requestDTO.setAccommodationId(accommodation.getId());
        requestDTO.setGuestId(sessionManager.getUserId());
        requestDTO.setTotalPrice(0.0);
    }

    private void showPricing(ArrayList<SeasonalRatesPricing> rates) {
        requestDTO.setTotalPrice(0.0);
        LinearLayout seasonalRatesLayout = view.findViewById(R.id.seasonalRatesLayout);
        seasonalRatesLayout.removeAllViewsInLayout();

        for (SeasonalRatesPricing rate : rates) {
            double price = 0;
            LinearLayout rateLayout = new LinearLayout(requireActivity());
            rateLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            rateLayout.setLayoutParams(layoutParams);

            TextView pricePerNight = new TextView(requireContext());
            pricePerNight.setText(rate.getPrice() + " rsd x " + rate.getNumberOfNights() + " nights");
            pricePerNight.setTextSize(14);
            pricePerNight.setTextColor(Color.rgb(152, 146, 143));
            rateLayout.addView(pricePerNight);
            price += rate.getPrice() * rate.getNumberOfNights();

            if(accommodation.getIsPricePerGuest()) {
                TextView guest = new TextView(requireContext());
                guest.setText(" x " + requestDTO.getNumberOfGuests() + " guests       ");
                guest.setTextSize(14);
                guest.setTextColor(Color.rgb(152, 146, 143));
                rateLayout.addView(guest);
                price *= requestDTO.getNumberOfGuests();
            }

            TextView dateRange = new TextView(requireContext());
            dateRange.setText(rate.getStartDate().split("T")[0] + " - " + rate.getEndDate().split("T")[0]);
            dateRange.setTextSize(14);
            dateRange.setTextColor(Color.rgb(152, 146, 143));
            rateLayout.addView(dateRange);

            requestDTO.setTotalPrice(requestDTO.getTotalPrice() + price);
            seasonalRatesLayout.addView(rateLayout);
        }

        TextView totalPriceTextView = new TextView(requireContext());
        totalPriceTextView.setText("Total price: " + requestDTO.getTotalPrice() + " rsd");
        totalPriceTextView.setTextSize(14);
        totalPriceTextView.setTextColor(Color.rgb(152, 146, 143));
        seasonalRatesLayout.addView(totalPriceTextView);

    }

    private void getPricing() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        AccommodationSeasonalRate accommodationSeasonalRateDTO = new AccommodationSeasonalRate(
                accommodation.getId(),
                dateFormat.format(selectedStartDate),
                dateFormat.format(selectedEndDate));
        Call<ArrayList<SeasonalRatesPricing>> call = ClientUtils.accommodationService.getPricing(accommodationSeasonalRateDTO);
        call.enqueue(new Callback<ArrayList<SeasonalRatesPricing>>() {
            @Override
            public void onResponse(Call<ArrayList<SeasonalRatesPricing>> call, Response<ArrayList<SeasonalRatesPricing>> response) {
                if(response.isSuccessful()) {
                    ArrayList<SeasonalRatesPricing> rates = response.body();
                    showPricing(rates);
                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SeasonalRatesPricing>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void makeReservationRequest() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        requestDTO.setStartDate(dateFormat.format(selectedStartDate));
        requestDTO.setEndDate(dateFormat.format(selectedEndDate));

        Call<MakeReservationRequest> call = ClientUtils.reservationRequestService.makeReservationRequest(requestDTO);
        call.enqueue(new Callback<MakeReservationRequest>() {
            @Override
            public void onResponse(Call<MakeReservationRequest> call, Response<MakeReservationRequest> response) {
                if(response.isSuccessful()) {
                    showSnackbar("Reservation request successful!");

                    sendNotification(accommodation.getHost(), "New reservation request for " + accommodation.getName(),
                            NotificationType.NEW_RESERVATION_REQUEST.getTypeMessage());

                } else {
                    showSnackbar("Reservation request failed!");
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
                Button makeReservationButton = view.findViewById(R.id.make_reservation_request_button);
                makeReservationButton.setEnabled(false);
            }

            @Override
            public void onFailure(Call<MakeReservationRequest> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    public void makeStartDatePicker() {
        Button startDateButton = view.findViewById(R.id.reservationStartDateButton);

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
                    if(!accommodation.isDateRangeAvailable(date, selectedEndDate.getTime())) return false;
                }
                if(date < currentDate)
                    return false;

                return accommodation.isDateAvailable(date);
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

                        Button makeReservationButton = view.findViewById(R.id.make_reservation_request_button);
                        EditText numberOfGuestsEditText = view.findViewById(R.id.numberOfGuestsEditText);
                        if(!numberOfGuestsEditText.getText().toString().trim().isEmpty() && selectedStartDate != null && selectedEndDate != null) {
                            makeReservationButton.setEnabled(true);
                            getPricing();
                        }
                    }
                }
        );
    }

    public void makeEndDatePicker() {
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
                long currentDate = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                if(selectedStartDate != null) {
                    if(date < selectedStartDate.getTime()) return false;
                    if(!accommodation.isDateRangeAvailable(selectedStartDate.getTime(), date)) return false;
                }
                if(date < currentDate)
                    return false;

                return accommodation.isDateAvailable(date);
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

                        Button makeReservationButton = view.findViewById(R.id.make_reservation_request_button);
                        EditText numberOfGuestsEditText = view.findViewById(R.id.numberOfGuestsEditText);
                        if(!numberOfGuestsEditText.getText().toString().trim().isEmpty() && selectedStartDate != null && selectedEndDate != null) {
                            makeReservationButton.setEnabled(true);
                            getPricing();
                        }
                    }
                }
        );
    }

    private void makeFilterForGuestNumber() {
        EditText numberOfGuestsEditText = view.findViewById(R.id.numberOfGuestsEditText);
        int min = accommodation.getMinGuests();
        int max = accommodation.getMaxGuests();
        numberOfGuestsEditText.setFilters(new InputFilter[]{ new InputFilterMinMax(min, max)});
    }

    private void showSnackbar(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    private void sendNotification(String username, String message, String type) {
        Intent intent = new Intent(requireActivity(), SocketService.class);
        intent.putExtra("username", username);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        intent.setAction(SocketService.ACTION_SEND_NOTIFICATION);
        requireActivity().startForegroundService(intent);
    }
}