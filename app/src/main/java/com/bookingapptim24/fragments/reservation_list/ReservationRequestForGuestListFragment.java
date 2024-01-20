package com.bookingapptim24.fragments.reservation_list;

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
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentReservationRequestForGuestListBinding;
import com.bookingapptim24.databinding.FragmentReservationRequestForHostListBinding;
import com.bookingapptim24.models.ReservationRequestForGuest;
import com.bookingapptim24.models.ReservationRequestForHost;
import com.bookingapptim24.models.SearchAndFilterReservationRequests;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRequestForGuestListFragment extends ListFragment implements DataChangesListener {

    private ReservationRequestForGuestListAdapter adapter;
    private FragmentReservationRequestForGuestListBinding binding;
    private SessionManager sessionManager;
    private ArrayList<ReservationRequestForGuest> requests = new ArrayList<>();
    private SearchAndFilterReservationRequests searchAndFilterDTO;

    public ReservationRequestForGuestListFragment(SearchAndFilterReservationRequests  searchAndFilterDTO) {
        this.searchAndFilterDTO = searchAndFilterDTO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView Reservation Request Guest List Fragment");
        binding = FragmentReservationRequestForGuestListBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(searchAndFilterDTO != null) {
            searchAndFilterRequests();
        } else {
            getData();
        }
        this.getListView().setDividerHeight(2);
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
        Long userId = sessionManager.getUserId();
        Call<ArrayList<ReservationRequestForGuest>> call = ClientUtils.reservationRequestService.getForGuest(userId);
        call.enqueue(new Callback<ArrayList<ReservationRequestForGuest>>() {
            @Override
            public void onResponse(Call<ArrayList<ReservationRequestForGuest>> call, Response<ArrayList<ReservationRequestForGuest>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    requests = response.body();
                    adapter = new ReservationRequestForGuestListAdapter(getActivity(), getActivity().getSupportFragmentManager(), requests);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setListener(ReservationRequestForGuestListFragment.this);
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReservationRequestForGuest>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onDataChanged() {
        if(searchAndFilterDTO != null) {
            searchAndFilterRequests();
        } else {
            getData();
        }
    }

    private void searchAndFilterRequests() {
        Long userId = sessionManager.getUserId();
        Call<ArrayList<ReservationRequestForGuest>> call = ClientUtils.reservationRequestService.searchRequestsForGuest(userId, searchAndFilterDTO);
        call.enqueue(new Callback<ArrayList<ReservationRequestForGuest>>() {
            @Override
            public void onResponse(Call<ArrayList<ReservationRequestForGuest>> call, Response<ArrayList<ReservationRequestForGuest>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    requests = response.body();
                    adapter = new ReservationRequestForGuestListAdapter(getActivity(), getActivity().getSupportFragmentManager(), requests);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setListener(ReservationRequestForGuestListFragment.this);
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReservationRequestForGuest>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}