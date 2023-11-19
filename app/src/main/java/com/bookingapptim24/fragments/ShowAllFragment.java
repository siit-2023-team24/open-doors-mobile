package com.bookingapptim24.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.AccommodationAdapter;
import com.bookingapptim24.FilterActivity;
import com.bookingapptim24.LoginScreen;
import com.bookingapptim24.R;
import com.bookingapptim24.RegisterScreen;
import com.bookingapptim24.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowAllFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private AccommodationAdapter accommodationAdapter;

    public ShowAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment ShowAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowAllFragment newInstance(String param1, String param2) {
        ShowAllFragment fragment = new ShowAllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Accommodation> accommodationList = getSampleAccommodations();
        accommodationAdapter = new AccommodationAdapter(accommodationList, requireContext());
        recyclerView.setAdapter(accommodationAdapter);

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SearchActivity.class);
                startActivity(intent);
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

    private List<Accommodation> getSampleAccommodations() {
        List<Accommodation> accommodations = new ArrayList<>();
        accommodations.add(new Accommodation("Accommodation 1", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 2", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 3", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 4", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 5", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 6", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 7", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 8", R.drawable.accommodation_image, 4.5, 1500));
        accommodations.add(new Accommodation("Accommodation 9", R.drawable.accommodation_image, 4.5, 1500));

        return accommodations;
    }
}