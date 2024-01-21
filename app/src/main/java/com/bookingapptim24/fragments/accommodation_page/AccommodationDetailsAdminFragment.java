package com.bookingapptim24.fragments.accommodation_page;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.fragments.FragmentTransition;
import com.bookingapptim24.models.PendingAccommodationHost;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationDetailsAdminFragment extends Fragment {

    private SessionManager sessionManager;

    private static final String ARG_ID = "id";
    private static final String ARG_ACCOMMODATION_ID = "accommodationId";

    private PendingAccommodationHost dto = new PendingAccommodationHost();


    public AccommodationDetailsAdminFragment() {
    }

    public static AccommodationDetailsAdminFragment newInstance(Long id, Long accommodationId) {
        AccommodationDetailsAdminFragment fragment = new AccommodationDetailsAdminFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_ACCOMMODATION_ID, accommodationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dto.setId(getArguments().getLong(ARG_ID));
            dto.setAccommodationId(getArguments().getLong(ARG_ACCOMMODATION_ID));
        }
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "Accommodation Details Admin onCreateView()");
        View view = inflater.inflate(R.layout.fragment_accommodation_details_admin, container, false);

        Button approveBtn = view.findViewById(R.id.approve_accommodation_btn);
        approveBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
            dialog.setMessage("Are you sure you want to approve this accommodation?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onApprove();
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button denyBtn = view.findViewById(R.id.deny_accommodation_btn);
        denyBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
            dialog.setMessage("Are you sure you want to deny this accommodation?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDeny();
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });
        FragmentTransition.to(AccommodationDetailsFragment.newInstance(dto.getId(), dto.getAccommodationId()),
                requireActivity(), false, R.id.accommodation_details);
        return view;
    }


    private void onApprove() {
        Log.d("OpenDoors", "Approve pending accommodation " + dto.getId());
        Call<ResponseBody> call = ClientUtils.pendingAccommodationService.approve(dto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(requireActivity(),"Accommodation approved", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    navController.navigate(R.id.nav_pending_accommodations);
                } else {
                    Log.d("OpenDoors", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    public void onDeny() {
        Log.d("OpenDoors", "Denying pending accommodation " + dto.getId());
        Call<ResponseBody> call = ClientUtils.pendingAccommodationService.deny(dto.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(requireActivity(), "Accommodation denied", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    navController.navigate(R.id.nav_pending_accommodations);
                } else {
                    Log.d("OpenDoors", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

}