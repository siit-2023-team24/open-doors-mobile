package com.bookingapptim24.fragments.home_page;

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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.SearchAndFilterAccommodations;
import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterAccommodationsFragment extends Fragment {

    private View view;
    private SearchAndFilterAccommodations filterDTO;
    private ArrayList<String> accommodationTypes;
    private ArrayList<String> amenities;

    public FilterAccommodationsFragment() {}

    public static FilterAccommodationsFragment newInstance() {
        FilterAccommodationsFragment fragment = new FilterAccommodationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_accommodations, container, false);

        getAccommodationTypes();
        getAmenities();

        Button filterButton = view.findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterAccommodations();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        ArrayList<SearchAndFilterAccommodations> searchAndFilters = (ArrayList<SearchAndFilterAccommodations>) args.getSerializable("searchAndFilterDTO");
        filterDTO = searchAndFilters.get(0);
        loadAccommodationTypes();
        loadAmenities();
        EditText startPriceEditText = view.findViewById(R.id.startPriceEditText);
        EditText endPriceEditText = view.findViewById(R.id.endPriceEditText);
        if(filterDTO.getStartPrice() != null)
            startPriceEditText.setText(filterDTO.getStartPrice().toString());
        if(filterDTO.getEndPrice() != null)
            endPriceEditText.setText(filterDTO.getEndPrice().toString());
    }

    private void getAccommodationTypes() {
        Call<ArrayList<String>> call = ClientUtils.accommodationService.getAccommodationTypes();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()) {
                    accommodationTypes = response.body();
                    loadAccommodationTypes();
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    private void getAmenities() {
        Call<ArrayList<String>> call = ClientUtils.accommodationService.getAmenities();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()) {
                    amenities = response.body();
                    loadAmenities();
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    private void loadAccommodationTypes() {
        LinearLayout accommodationTypesLayout = view.findViewById(R.id.accommodationTypesLayout);
        accommodationTypesLayout.removeAllViewsInLayout();

        if(accommodationTypes != null) {
            for (String accommodationType: accommodationTypes) {
                CheckBox checkBox = new CheckBox(requireContext());
                checkBox.setText(accommodationType);

                // Check if the type is in searchAndFilterDTO
                if (filterDTO.getTypes() != null &&
                        filterDTO.getTypes().contains(AccommodationType.valueOf(accommodationType))) {
                    checkBox.setChecked(true);
                }

                accommodationTypesLayout.addView(checkBox);
            }
        }
    }

    private void loadAmenities() {
        LinearLayout amenitiesLayout = view.findViewById(R.id.amenitiesFilterLayout);
        amenitiesLayout.removeAllViewsInLayout();

        if(amenities != null) {
            for (String amenity : amenities) {
                CheckBox checkBox = new CheckBox(requireContext());
                checkBox.setText(amenity);

                // Check if the amenity is in searchAndFilterDTO
                if (filterDTO.getAmenities() != null &&
                        filterDTO.getAmenities().contains(Amenity.valueOf(amenity))) {
                    checkBox.setChecked(true);
                }

                amenitiesLayout.addView(checkBox);
            }
        }
    }

    private void filterAccommodations() {
        getCheckedAccommodationTypes();
        getCheckedAmenities();

        EditText startPriceEditText = view.findViewById(R.id.startPriceEditText);
        EditText endPriceEditText = view.findViewById(R.id.endPriceEditText);
        String startPriceText = startPriceEditText.getText().toString().trim();
        String endPriceText = endPriceEditText.getText().toString().trim();

        if(!startPriceText.isEmpty()) {
            filterDTO.setStartPrice(Double.valueOf(startPriceText));
        } else {
            filterDTO.setStartPrice(null);
        }

        if(!endPriceText.isEmpty()) {
            filterDTO.setEndPrice(Double.valueOf(endPriceText));
        } else {
            filterDTO.setEndPrice(null);
        }

        ArrayList<SearchAndFilterAccommodations> searchAndFilters = new ArrayList<>();
        searchAndFilters.add(filterDTO);

        Bundle args = new Bundle();
        args.putSerializable("searchAndFilterDTO", searchAndFilters);

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.popBackStack();
        navController.navigate(R.id.nav_show_all, args);
    }

    private void getCheckedAccommodationTypes() {
        LinearLayout accommodationTypesLayout = view.findViewById(R.id.accommodationTypesLayout);
        Set<AccommodationType> checkedAccommodationTypes = EnumSet.noneOf(AccommodationType.class);

        for (int i = 0; i < accommodationTypesLayout.getChildCount(); i++) {
            View childView = accommodationTypesLayout.getChildAt(i);

            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                if (checkBox.isChecked()) {
                    try {
                        AccommodationType accommodationType = AccommodationType.valueOf(checkBox.getText().toString());
                        checkedAccommodationTypes.add(accommodationType);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum constant, if needed
                        e.printStackTrace();
                    }
                }
            }
        }

        filterDTO.setTypes(checkedAccommodationTypes);
    }

    private void getCheckedAmenities() {
        LinearLayout amenitiesLayout = view.findViewById(R.id.amenitiesFilterLayout);
        Set<Amenity> checkedAmenities = EnumSet.noneOf(Amenity.class);

        for (int i = 0; i < amenitiesLayout.getChildCount(); i++) {
            View childView = amenitiesLayout.getChildAt(i);

            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                if (checkBox.isChecked()) {
                    try {
                        Amenity amenity = Amenity.valueOf(checkBox.getText().toString());
                        checkedAmenities.add(amenity);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum constant, if needed
                        e.printStackTrace();
                    }
                }
            }
        }

        filterDTO.setAmenities(checkedAmenities);
    }

}