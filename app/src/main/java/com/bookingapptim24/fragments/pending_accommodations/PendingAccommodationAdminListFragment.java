package com.bookingapptim24.fragments.pending_accommodations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.databinding.FragmentPendingAccommodationAdminListBinding;
import com.bookingapptim24.databinding.FragmentPendingAccommodationHostListBinding;
import com.bookingapptim24.models.PendingAccommodationHost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingAccommodationAdminListFragment extends ListFragment {

    private PendingAccommodationHostListAdapter adapter;
    private FragmentPendingAccommodationAdminListBinding binding;

    private ArrayList<PendingAccommodationHost> accommodations = new ArrayList<>();


    public static PendingAccommodationAdminListFragment newInstance() {
        PendingAccommodationAdminListFragment fragment = new PendingAccommodationAdminListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView PendingAccommodationAdmin List Fragment");
        binding = FragmentPendingAccommodationAdminListBinding.inflate(inflater, container, false);
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
        Call<ArrayList<PendingAccommodationHost>> call = ClientUtils.pendingAccommodationService.getAll();
        call.enqueue(new Callback<ArrayList<PendingAccommodationHost>>() {
            @Override
            public void onResponse(Call<ArrayList<PendingAccommodationHost>> call, Response<ArrayList<PendingAccommodationHost>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    accommodations = response.body();
                    adapter = new PendingAccommodationHostListAdapter(getActivity(), getActivity().getSupportFragmentManager(), accommodations);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PendingAccommodationHost>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }


}