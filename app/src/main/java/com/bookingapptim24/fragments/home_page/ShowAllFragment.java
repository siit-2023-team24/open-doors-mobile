package com.bookingapptim24.fragments.home_page;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.models.SearchAndFilterAccommodations;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllFragment extends Fragment {

    private ArrayList<AccommodationSearchDTO> accommodations;
    private RecyclerView recyclerView;
    private HomePageAccommodationAdapter homePageAccommodationAdapter;
    private SearchAndFilterAccommodations searchAndFilterDTO;

    public ShowAllFragment() {}

    public ShowAllFragment(ArrayList<AccommodationSearchDTO> accommodations) {
        this.accommodations = accommodations;
    }

    public static ShowAllFragment newInstance(ArrayList<AccommodationSearchDTO> accommodations) {
        ShowAllFragment fragment = new ShowAllFragment(accommodations);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            accommodations = (ArrayList<AccommodationSearchDTO>) args.getSerializable("accommodations");
            ArrayList<SearchAndFilterAccommodations> searchAndFilters = (ArrayList<SearchAndFilterAccommodations>) args.getSerializable("searchAndFilterDTO");
            searchAndFilterDTO = searchAndFilters.get(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if(accommodations == null)
            fetchAccommodationsFromServer();
        loadAccommodations();

        if(searchAndFilterDTO == null)
            searchAndFilterDTO = new SearchAndFilterAccommodations();

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SearchAndFilterAccommodations> searchAndFilters = new ArrayList<>();
                searchAndFilters.add(searchAndFilterDTO);
                Bundle args = new Bundle();
                args.putSerializable("searchAndFilterDTO", searchAndFilters);

                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.fragment_search_accommodations, args);
            }
        });

        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SearchAndFilterAccommodations> searchAndFilters = new ArrayList<>();
                searchAndFilters.add(searchAndFilterDTO);
                Bundle args = new Bundle();
                args.putSerializable("searchAndFilterDTO", searchAndFilters);

                NavController navController = Navigation.findNavController((Activity) requireContext(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.fragment_filter_accommodations, args);
            }
        });

        return view;
    }

    private void fetchAccommodationsFromServer() {
        Call<ArrayList<AccommodationSearchDTO>> call = ClientUtils.accommodationService.getAll();
        call.enqueue(new Callback<ArrayList<AccommodationSearchDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationSearchDTO>> call, Response<ArrayList<AccommodationSearchDTO>> response) {
                if(response.isSuccessful()) {
                    accommodations = response.body();
                    loadAccommodations();
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

    private void loadAccommodations() {
        homePageAccommodationAdapter = new HomePageAccommodationAdapter(accommodations, requireContext());
        recyclerView.setAdapter(homePageAccommodationAdapter);
    }

    private void showSnackbar(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

}