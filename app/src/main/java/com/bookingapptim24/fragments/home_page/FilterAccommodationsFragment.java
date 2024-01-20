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
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.models.SearchAndFilterAccommodations;
import com.bookingapptim24.models.enums.AccommodationType;
import com.bookingapptim24.models.enums.Amenity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterAccommodationsFragment extends Fragment {

    private View view;
    private SearchAndFilterAccommodations searchAndFilterDTO;
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
                getCheckedAccommodationTypes();
                getCheckedAmenities();

                EditText startPriceEditText = view.findViewById(R.id.startPriceEditText);
                EditText endPriceEditText = view.findViewById(R.id.endPriceEditText);
                String startPriceText = startPriceEditText.getText().toString().trim();
                String endPriceText = endPriceEditText.getText().toString().trim();

                if(!startPriceText.isEmpty())
                    searchAndFilterDTO.setStartPrice(Double.valueOf(startPriceText));
                if(!endPriceText.isEmpty())
                    searchAndFilterDTO.setEndPrice(Double.valueOf(endPriceText));

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
        searchAndFilterDTO = searchAndFilters.get(0);
        loadAccommodationTypes();
        loadAmenities();
        EditText startPriceEditText = view.findViewById(R.id.startPriceEditText);
        EditText endPriceEditText = view.findViewById(R.id.endPriceEditText);
        if(searchAndFilterDTO.getStartPrice() != null)
            startPriceEditText.setText(searchAndFilterDTO.getStartPrice().toString());
        if(searchAndFilterDTO.getEndPrice() != null)
            endPriceEditText.setText(searchAndFilterDTO.getEndPrice().toString());
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
                if (searchAndFilterDTO.getTypes() != null &&
                        searchAndFilterDTO.getTypes().contains(AccommodationType.valueOf(accommodationType))) {
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
                if (searchAndFilterDTO.getAmenities() != null &&
                        searchAndFilterDTO.getAmenities().contains(Amenity.valueOf(amenity))) {
                    checkBox.setChecked(true);
                }

                amenitiesLayout.addView(checkBox);
            }
        }
    }

    private void filterAccommodations() {
        Log.d("FilterDTO", searchAndFilterDTO.toString());
        Call<ArrayList<AccommodationSearchDTO>> call = ClientUtils.accommodationService.searchAccommodations(searchAndFilterDTO);
        call.enqueue(new Callback<ArrayList<AccommodationSearchDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationSearchDTO>> call, Response<ArrayList<AccommodationSearchDTO>> response) {
                if(response.isSuccessful()) {
                    ArrayList<AccommodationSearchDTO> accommodations = response.body();
                    for(AccommodationSearchDTO a : accommodations)
                        Log.d("REZ", a.toString());

                    ArrayList<SearchAndFilterAccommodations> searchAndFilters = new ArrayList<>();
                    searchAndFilters.add(searchAndFilterDTO);

                    Bundle args = new Bundle();
                    args.putSerializable("accommodations", accommodations);
                    args.putSerializable("searchAndFilterDTO", searchAndFilters);

                    NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
                    navController.popBackStack();
                    navController.navigate(R.id.nav_show_all, args);

                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationSearchDTO>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
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

        searchAndFilterDTO.setTypes(checkedAccommodationTypes);
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

        searchAndFilterDTO.setAmenities(checkedAmenities);
    }

}