package com.bookingapptim24.fragments.reservation_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentReservationRequestForHostListBinding;
import com.bookingapptim24.models.ReservationRequestForHost;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRequestForHostListFragment extends ListFragment implements DataChangesListener {

    private ReservationRequestForHostListAdapter adapter;
    private FragmentReservationRequestForHostListBinding binding;
    private SessionManager sessionManager;
    private ArrayList<ReservationRequestForHost> requests = new ArrayList<>();

    public ReservationRequestForHostListFragment() {}

    public static ReservationRequestForHostListFragment newInstance() {
        return new ReservationRequestForHostListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView Reservation Request Host List Fragment");
        binding = FragmentReservationRequestForHostListBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireContext());
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
        Long userId = sessionManager.getUserId();
        Call<ArrayList<ReservationRequestForHost>> call = ClientUtils.reservationRequestService.getForHost(userId);
        call.enqueue(new Callback<ArrayList<ReservationRequestForHost>>() {
            @Override
            public void onResponse(Call<ArrayList<ReservationRequestForHost>> call, Response<ArrayList<ReservationRequestForHost>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    requests = response.body();
                    adapter = new ReservationRequestForHostListAdapter(getActivity(), getActivity().getSupportFragmentManager(), requests);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setListener(ReservationRequestForHostListFragment.this);
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReservationRequestForHost>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onDataChanged() {
        getData();
    }
}