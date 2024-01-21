package com.bookingapptim24.fragments.home_page;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bookingapptim24.R;
import com.bookingapptim24.models.SearchAndFilterAccommodations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchAccommodationsFragment extends Fragment implements SensorEventListener {
    private View view;
    private SearchAndFilterAccommodations searchDTO;
    private Timestamp selectedStartDate;
    private Timestamp selectedEndDate;

    private ImageView landmarkImage;
    private SensorManager sensorManager;
    private Sensor magnetometerSensor;

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
        landmarkImage = view.findViewById(R.id.landmark_img);
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
                searchAccommodations();
            }
        });

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Check if the magnetometer sensor is available
        if (magnetometerSensor == null) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.no_magnetometer), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        ArrayList<SearchAndFilterAccommodations> searchAndFilters = (ArrayList<SearchAndFilterAccommodations>) args.getSerializable("searchAndFilterDTO");
        searchDTO = searchAndFilters.get(0);
        Log.d("sfDTO", searchDTO.toString());
        if(searchDTO.getLocation() != null) {
            EditText location = view.findViewById(R.id.locationEditText);
            location.setText(searchDTO.getLocation());
        }
        if(searchDTO.getGuestNumber() != null) {
            EditText numOfGuests = view.findViewById(R.id.numberOfGuestsEditText);
            numOfGuests.setText(searchDTO.getGuestNumber().toString());
        }
        searchDTO.setStartDate(null);
        searchDTO.setEndDate(null);

        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void searchAccommodations() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(selectedStartDate != null)
            searchDTO.setStartDate(dateFormat.format(selectedStartDate));
        if(selectedEndDate != null)
            searchDTO.setEndDate(dateFormat.format(selectedEndDate));

        EditText location = view.findViewById(R.id.locationEditText);
        EditText numOfGuests = view.findViewById(R.id.numberOfGuestsEditText);
        String locationText = location.getText().toString().trim();
        String numOfGuestsText = numOfGuests.getText().toString().trim();

        if(!locationText.isEmpty())
            searchDTO.setLocation(locationText);
        else
            searchDTO.setLocation(null);

        if (!numOfGuestsText.isEmpty()) {
            try {
                int numOfGuestsValue = Integer.parseInt(numOfGuestsText);
                searchDTO.setGuestNumber(numOfGuestsValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            searchDTO.setGuestNumber(null);
        }
        sensorManager.unregisterListener(this);
    }

        Log.d("FilterAccommodations", searchDTO.toString());

        ArrayList<SearchAndFilterAccommodations> searchAndFilters = new ArrayList<>();
        searchAndFilters.add(searchDTO);

        Bundle args = new Bundle();
        args.putSerializable("searchAndFilterDTO", searchAndFilters);

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.popBackStack();
        navController.navigate(R.id.nav_show_all, args);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] magneticValues = event.values;

        // Calculate azimuth (heading) in degrees
        float azimuth = (float) Math.toDegrees(Math.atan2(magneticValues[1], magneticValues[0]));

        // Ensure azimuth is between 0 and 360 degrees
        azimuth = (azimuth + 360) % 360;

        // Update the image based on the device's orientation
        updateImageBasedOnDirection(azimuth);
    }

    private void updateImageBasedOnDirection(float azimuth) {
        // Define directions based on azimuth ranges
        // You may need to adjust these ranges based on your specific use case
        if (isFacingNorth(azimuth)) {
            landmarkImage.setImageResource(R.drawable.land_of_ice_and_fire);
        } else if (isFacingEast(azimuth)) {
            landmarkImage.setImageResource(R.drawable.land_of_the_rising_sun);
        } else if (isFacingSouth(azimuth)) {
            landmarkImage.setImageResource(R.drawable.land_down_under);
        } else if (isFacingWest(azimuth)) {
            landmarkImage.setImageResource(R.drawable.land_of_the_free);
        }
    }

    private boolean isFacingNorth(float azimuth) {
        return azimuth >= 45 && azimuth < 135;
    }
    private boolean isFacingEast(float azimuth) {
        return azimuth >= 135 && azimuth < 225;
    }
    private boolean isFacingSouth(float azimuth) {
        return azimuth >= 225 && azimuth < 315;
    }
    private boolean isFacingWest(float azimuth) {
        return azimuth >= 315 || azimuth < 45;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}