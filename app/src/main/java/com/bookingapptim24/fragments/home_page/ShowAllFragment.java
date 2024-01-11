package com.bookingapptim24.fragments.home_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookingapptim24.FilterActivity;
import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomePageAccommodationAdapter homePageAccommodationAdapter;

    public ShowAllFragment() {}

    public static ShowAllFragment newInstance() {
        ShowAllFragment fragment = new ShowAllFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        fetchAccommodationsFromServer(view);

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use FragmentTransaction to replace the current fragment with the search fragment
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new SearchFragment());
//                transaction.addToBackStack(null);  // Optional: Add to back stack if you want to navigate back
//                transaction.commit();
            }
        });

        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchAccommodationsFromServer(View view) {
        Call<ArrayList<AccommodationSearchDTO>> call = ClientUtils.accommodationService.getAll();
        call.enqueue(new Callback<ArrayList<AccommodationSearchDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationSearchDTO>> call, Response<ArrayList<AccommodationSearchDTO>> response) {
                if(response.isSuccessful()) {
                    List<AccommodationSearchDTO> accommodations = response.body();
                    homePageAccommodationAdapter = new HomePageAccommodationAdapter(accommodations, requireContext());
                    recyclerView.setAdapter(homePageAccommodationAdapter);
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
    private void showSnackbar(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

}