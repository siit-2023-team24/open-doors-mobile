package com.bookingapptim24.fragments.accommodation_page;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bookingapptim24.R;
import com.bookingapptim24.activities.CreateAccommodationActivity;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.fragments.FragmentTransition;
import com.bookingapptim24.models.AccommodationWithTotalPrice;
import com.bookingapptim24.models.PendingAccommodationHost;

import org.json.JSONObject;


import java.io.IOException;

import java.util.ArrayList;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationDetailsFragment extends Fragment {

    private SessionManager sessionManager;
    private View detailsView;

    private static final String ARG_ID = "id";
    private static final String ARG_ACCOMMODATION_ID = "accommodationId";

    private PendingAccommodationHost dto = new PendingAccommodationHost();
    private AccommodationWithTotalPrice accommodationDetails = new AccommodationWithTotalPrice();


    public AccommodationDetailsFragment() {
    }

    public static AccommodationDetailsFragment newInstance(Long id, Long accommodationId) {
        AccommodationDetailsFragment fragment = new AccommodationDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_ACCOMMODATION_ID, accommodationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            dto.setId(getArguments().getLong(ARG_ID));
            dto.setAccommodationId(getArguments().getLong(ARG_ACCOMMODATION_ID));
        }
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "Accommodation Details onCreateView()");
        View view;
        String role = null;
        if(sessionManager.isLoggedIn()) {
            role = sessionManager.getRole();
        }
        String username = sessionManager.getUsername();

        if(role == null || !role.equals("ROLE_GUEST")) {
            view = inflater.inflate(R.layout.fragment_accommodation_details, container, false);
            detailsView = view;
            Log.i("Fragment inflated", "Inflated accommodation_details");
        } else {
            view = inflater.inflate(R.layout.accommodation_details_guest, container, false);
            detailsView = view;
            Log.i("Fragment inflated", "Inflated accommodation_details_guest");

            Button makeReservationButton = view.findViewById(R.id.make_reservation_button);
            makeReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    ArrayList<AccommodationWithTotalPrice> accommodationContainer = new ArrayList<>();
                    accommodationContainer.add(accommodationDetails);
                    args.putSerializable("accommodationContainer", accommodationContainer);
                    NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
                    navController.navigate(R.id.nav_fragment_make_reservation_request, args);
                }
            });

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        Log.d("OpenDoors", "Getting accommodation details: " + dto.getId() + ", " + dto.getAccommodationId());
        if (dto.getId() > 0) {
            //get pending accommodation
            Call<AccommodationWithTotalPrice> call = ClientUtils.pendingAccommodationService.getDetails(dto.getId());
            call.enqueue(new Callback<AccommodationWithTotalPrice>() {
                @Override
                public void onResponse(Call<AccommodationWithTotalPrice> call, Response<AccommodationWithTotalPrice> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received pending accommodation: " + dto.getId());
                        accommodationDetails = response.body();
                        updateViews();
                    } else {
                        Log.d("OpenDoors","Message received: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<AccommodationWithTotalPrice> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });

        } else {
            //get active accommodation
            Call<AccommodationWithTotalPrice> call = ClientUtils.accommodationService.getAccommodation(dto.getAccommodationId());
            call.enqueue(new Callback<AccommodationWithTotalPrice>() {
                @Override
                public void onResponse(Call<AccommodationWithTotalPrice> call, Response<AccommodationWithTotalPrice> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received accommodation: " + dto.getAccommodationId());
                        accommodationDetails = response.body();
                        updateViews();
                    } else {
                        Log.d("OpenDoors","Message received: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<AccommodationWithTotalPrice> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    }

    private void updateViews() {
        View view = detailsView;
        // Retrieve views from the layout
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView cityCountryTextView = view.findViewById(R.id.cityCountryTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        Button hostBtn = view.findViewById(R.id.host_btn);
        Button reviewsBtn = view.findViewById(R.id.reviews_btn);

        // Set data in views
        //Glide.with(requireContext()).load(accommodation.getImage()).into(imageView);
        nameTextView.setText(accommodationDetails.getName());
        cityCountryTextView.setText(accommodationDetails.getCity() + ", " + accommodationDetails.getCountry());
        if(accommodationDetails.getAverageRating() != null) {
            ratingTextView.setText(String.valueOf(accommodationDetails.getAverageRating()));
            ratingTextView.setVisibility(View.VISIBLE);
        } else {
            ratingTextView.setVisibility(View.GONE);
        }
        descriptionTextView.setText(accommodationDetails.getDescription());

        // Add amenities dynamically
        LinearLayout amenitiesLayout = view.findViewById(R.id.amenitiesLayout);
        for (String amenity : accommodationDetails.getAmenities()) {
            TextView amenityTextView = new TextView(requireContext());
            amenityTextView.setText(amenity);
            amenityTextView.setTextSize(16);
            amenitiesLayout.addView(amenityTextView);
        }
        hostBtn.setText(accommodationDetails.getHost());
        hostBtn.setOnClickListener(v -> {
            openHostReviewsFragment();
        });
        reviewsBtn.setOnClickListener(v -> {
            openAccommodationReviewsFragment();
        });
    }


    private void openHostReviewsFragment() {
        Bundle args = new Bundle();
        args.putLong("hostId", accommodationDetails.getHostId());
        args.putString("hostUsername", accommodationDetails.getHost());

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.navigate(R.id.host_reviews, args);
    }


    private void openAccommodationReviewsFragment() {
        Bundle args = new Bundle();
        args.putLong("accommodationId", accommodationDetails.getId());
        args.putString("hostUsername", accommodationDetails.getHost());

        NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
        navController.navigate(R.id.accommodation_reviews, args);
    }
}