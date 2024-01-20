package com.bookingapptim24.fragments.home_page;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private ArrayList<AccommodationSearchDTO> accommodations;
    private RecyclerView recyclerView;
    private HomePageAccommodationAdapter homePageAccommodationAdapter;
    private SearchAndFilterAccommodations searchAndFilterDTO;
    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;

    private boolean isAscending = true;

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
        else
            sortAscending();
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
                navController.navigate(R.id.nav_fragment_search_accommodations, args);
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
                navController.navigate(R.id.nav_fragment_filter_accommodations, args);
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
                    sortAscending();
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

    private void sortAscending() {
        isAscending = true;
        accommodations.sort(Comparator.comparingDouble(AccommodationSearchDTO::getPrice));
    }

    private void sortDescending() {
        isAscending = false;

        accommodations.sort(Comparator.comparingDouble(AccommodationSearchDTO::getPrice).reversed());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 200) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = event.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD && accommodations != null) {
                    Log.d("Harlem", "Shake");
                    Log.d("list", accommodations.toString());
                    if(isAscending) {
                        sortDescending();
                        Toast.makeText(requireContext(), requireContext().getString(R.string.sorted_descending), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        sortAscending();
                        Toast.makeText(requireContext(), requireContext().getString(R.string.sorted_ascending), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("list", accommodations.toString());
                    homePageAccommodationAdapter.notifyDataSetChanged();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.i("REZ_ACCELEROMETER", String.valueOf(accuracy));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

}