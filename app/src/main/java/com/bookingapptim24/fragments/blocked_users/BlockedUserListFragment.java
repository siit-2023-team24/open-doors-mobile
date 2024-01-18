package com.bookingapptim24.fragments.blocked_users;

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
import com.bookingapptim24.databinding.FragmentBlockedUserListBinding;
import com.bookingapptim24.databinding.FragmentUserReportListBinding;
import com.bookingapptim24.fragments.user_reports.UserReportAdapter;
import com.bookingapptim24.fragments.user_reports.UserReportListFragment;
import com.bookingapptim24.models.UserReport;
import com.bookingapptim24.models.UserSummary;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockedUserListFragment extends ListFragment {

    private BlockedUserAdapter adapter;
    private FragmentBlockedUserListBinding binding;
    private ArrayList<UserSummary> users = new ArrayList<>();

    public BlockedUserListFragment() {
    }

    public static BlockedUserListFragment newInstance() {
        return new BlockedUserListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView User Blocked List Fragment");
        binding = FragmentBlockedUserListBinding.inflate(inflater, container, false);
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
        Call<ArrayList<UserSummary>> call = ClientUtils.userService.getBlocked();
        call.enqueue(new Callback<ArrayList<UserSummary>>() {
            @Override
            public void onResponse(Call<ArrayList<UserSummary>> call, Response<ArrayList<UserSummary>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    users = response.body();
                    adapter = new BlockedUserAdapter(getActivity(), getActivity().getSupportFragmentManager(), users);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserSummary>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}