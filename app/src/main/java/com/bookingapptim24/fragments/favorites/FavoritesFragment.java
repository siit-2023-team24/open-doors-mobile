package com.bookingapptim24.fragments.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.fragments.home_page.HomePageAccommodationAdapter;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {

    private ArrayList<AccommodationSearchDTO> accommodations;
    private RecyclerView recyclerView;
    private HomePageAccommodationAdapter homePageAccommodationAdapter;
    private SessionManager sessionManager;

    public FavoritesFragment() {}

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFavorites();
    }


    private void fetchFavorites() {
        Call<ArrayList<AccommodationSearchDTO>> call = ClientUtils.accommodationService.getFavorites(sessionManager.getUserId());
        call.enqueue(new Callback<ArrayList<AccommodationSearchDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationSearchDTO>> call, Response<ArrayList<AccommodationSearchDTO>> response) {
                if (response.isSuccessful()) {
                    accommodations = response.body();
                    loadFavorites();
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationSearchDTO>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void loadFavorites() {
        homePageAccommodationAdapter = new HomePageAccommodationAdapter(accommodations, requireContext());
        recyclerView.setAdapter(homePageAccommodationAdapter);
    }
}