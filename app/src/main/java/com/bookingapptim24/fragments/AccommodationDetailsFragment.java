package com.bookingapptim24.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.PendingAccommodationHost;
import com.bookingapptim24.models.PendingAccommodationWhole;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationDetailsFragment extends Fragment {

    private SessionManager sessionManager;

    private static final String ARG_ID = "id";
    private static final String ARG_ACCOMMODATION_ID = "accommodationId";
    private static final String ARG_NAME = "name";
    private static final String ARG_IMAGE = "image";

    private PendingAccommodationHost dto = new PendingAccommodationHost();

    private PendingAccommodationWhole accommodation = new PendingAccommodationWhole();


    public AccommodationDetailsFragment() {
    }

    public static AccommodationDetailsFragment newInstance(Long id, Long accommodationId, String name, Long image) {
        AccommodationDetailsFragment fragment = new AccommodationDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_ACCOMMODATION_ID, accommodationId);
        args.putString(ARG_NAME, name);
        args.putLong(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            accommodation.setId(getArguments().getLong(ARG_ID));
            accommodation.setAccommodationId(getArguments().getLong(ARG_ACCOMMODATION_ID));

            dto.setId(getArguments().getLong(ARG_ID));
            dto.setAccommodationId(getArguments().getLong(ARG_ACCOMMODATION_ID));
            dto.setName(getArguments().getString(ARG_NAME));
            dto.setImage(getArguments().getLong(ARG_IMAGE));
        }
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        //call server to get resource
        //check if it's pending or active and call appropriate
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "Accommodation Details onCreateView()");
        View view;

        String role = sessionManager.getRole();

        if (role.equals("ROLE_ADMIN") && dto.getId() != null) {
            view = inflater.inflate(R.layout.accommodation_details_admin, container, false);

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

        } else if (role.equals("ROLE_HOST") && dto.getId() == 0) {
            view = inflater.inflate(R.layout.accommodation_details_host, container, false);
            //onclick edit and financial report

        }
        else  {
            view = inflater.inflate(R.layout.fragment_accommodation_details, container, false);
        }

        //set data in view

        return view;
    }

    private void onApprove() {
        Log.d("OpenDoors", "Approve pending accommodation " + accommodation.getId());
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
        Log.d("OpenDoors", "Denying pending accommodation " + accommodation.getId());
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