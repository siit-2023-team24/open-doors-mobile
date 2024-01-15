package com.bookingapptim24.fragments.user_reports;

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
import com.bookingapptim24.databinding.FragmentReportedReviewListBinding;
import com.bookingapptim24.databinding.FragmentUserReportListBinding;
import com.bookingapptim24.fragments.reported_reviews.ReportedReviewListAdapter;
import com.bookingapptim24.models.ReportedReview;
import com.bookingapptim24.models.UserReport;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserReportListFragment extends ListFragment implements DataChangesListener {

    private UserReportAdapter adapter;
    private FragmentUserReportListBinding binding;
    private ArrayList<UserReport> reports = new ArrayList<>();

    public UserReportListFragment() {}


    public static UserReportListFragment newInstance() {
        return new UserReportListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView User Report List Fragment");
        binding = FragmentUserReportListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OpenDoors", "onCreate AccommodationHost List Fragment");
        this.getListView().setDividerHeight(2);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getData() {
        Call<ArrayList<UserReport>> call = ClientUtils.userReportService.getAll();
        call.enqueue(new Callback<ArrayList<UserReport>>() {
            @Override
            public void onResponse(Call<ArrayList<UserReport>> call, Response<ArrayList<UserReport>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    reports = response.body();
                    adapter = new UserReportAdapter(getActivity(), getActivity().getSupportFragmentManager(), reports);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setListener(UserReportListFragment.this);

                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserReport>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    public void onDataChanged() {
        getData();
    }
}

