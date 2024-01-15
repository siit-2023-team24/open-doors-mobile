package com.bookingapptim24.fragments.accommodation_host;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.ListFragment;

import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentAccommodationHostListBinding;
import com.bookingapptim24.models.AccommodationHost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationHostListFragment extends ListFragment {
    private AccommodationHostListAdapter adapter;
    private FragmentAccommodationHostListBinding binding;
    private MenuProvider menuProvider;
    private SessionManager sessionManager;

    private ArrayList<AccommodationHost> accommodations = new ArrayList<>();

    public static AccommodationHostListFragment newInstance() {
        AccommodationHostListFragment fragment = new AccommodationHostListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView AccommodationHost List Fragment");
        sessionManager = new SessionManager(requireContext());
        binding = FragmentAccommodationHostListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getListView().setDividerHeight(2);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromClient();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getDataFromClient() {
        Long hostId = sessionManager.getUserId();
        Call<ArrayList<AccommodationHost>> call = ClientUtils.accommodationService.getForHost(hostId);
        call.enqueue(new Callback<ArrayList<AccommodationHost>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationHost>> call, Response<ArrayList<AccommodationHost>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    accommodations = response.body();
                    adapter = new AccommodationHostListAdapter(getActivity(), getActivity().getSupportFragmentManager(), accommodations);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationHost>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }



}
