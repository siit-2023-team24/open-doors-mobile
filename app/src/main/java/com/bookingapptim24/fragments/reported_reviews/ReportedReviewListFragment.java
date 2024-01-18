package com.bookingapptim24.fragments.reported_reviews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.databinding.FragmentReportedReviewListBinding;
import com.bookingapptim24.models.ReportedReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedReviewListFragment extends ListFragment {

    private ReportedReviewListAdapter adapter;
    private FragmentReportedReviewListBinding binding;
    private ArrayList<ReportedReview> reviews = new ArrayList<>();

    public ReportedReviewListFragment() {}

     public static ReportedReviewListFragment newInstance() {
         return new ReportedReviewListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView Reported Review List Fragment");
        binding = FragmentReportedReviewListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getListView().setDividerHeight(2);
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
        Call<ArrayList<ReportedReview>> call = ClientUtils.hostReviewService.getAllReported();
        call.enqueue(new Callback<ArrayList<ReportedReview>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportedReview>> call, Response<ArrayList<ReportedReview>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    reviews = response.body();
                    adapter = new ReportedReviewListAdapter(getActivity(), getActivity().getSupportFragmentManager(), reviews);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReportedReview>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}