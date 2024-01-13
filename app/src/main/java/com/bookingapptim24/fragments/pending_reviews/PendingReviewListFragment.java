package com.bookingapptim24.fragments.pending_reviews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.databinding.FragmentPendingReviewListBinding;
import com.bookingapptim24.fragments.accommodation_host.AccommodationHostListAdapter;
import com.bookingapptim24.models.PendingReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendingReviewListFragment extends ListFragment {

    private PendingReviewListAdapter adapter;
    private FragmentPendingReviewListBinding binding;

    private ArrayList<PendingReview> reviews = new ArrayList<>();


    public PendingReviewListFragment() {}

    public static PendingReviewListFragment newInstance() {
        return new PendingReviewListFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView Pending Review List Fragment");
        binding = FragmentPendingReviewListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OpenDoors", "onCreate AccommodationHost List Fragment");
        this.getListView().setDividerHeight(2);
//        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getData() {
        Call<ArrayList<PendingReview>> call = ClientUtils.accommodationReviewService.getAllPending();
        call.enqueue(new Callback<ArrayList<PendingReview>>() {
            @Override
            public void onResponse(Call<ArrayList<PendingReview>> call, Response<ArrayList<PendingReview>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    reviews = response.body();
                    adapter = new PendingReviewListAdapter(getActivity(), getActivity().getSupportFragmentManager(), reviews);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PendingReview>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }


}