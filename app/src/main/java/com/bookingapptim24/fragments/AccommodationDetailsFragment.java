package com.bookingapptim24.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.bookingapptim24.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private static final String ARG_MY = "fromMyList";
    private boolean fromMyList = false;

    private PendingAccommodationHost dto = new PendingAccommodationHost();

    private PendingAccommodationWhole accommodation = new PendingAccommodationWhole();


    public AccommodationDetailsFragment() {
    }

    public static AccommodationDetailsFragment newInstance(Long id, Long accommodationId, String name, Long image, boolean fromMyList) {
        AccommodationDetailsFragment fragment = new AccommodationDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_ACCOMMODATION_ID, accommodationId);
        args.putString(ARG_NAME, name);
        args.putLong(ARG_IMAGE, image);
        args.putBoolean(ARG_MY, fromMyList);
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

            fromMyList = getArguments().getBoolean(ARG_MY);
        }
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "Accommodation Details onCreateView()");
        View view;

        String role = sessionManager.getRole();
        String username = sessionManager.getUsername();

        if (role.equals("ROLE_ADMIN") && dto.getId() != 0) {
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

        } else if (role.equals("ROLE_HOST") && dto.getId() == 0 && fromMyList) {
            view = inflater.inflate(R.layout.accommodation_details_host, container, false);
            //onclick edit and financial report

            Button deleteBtn = view.findViewById(R.id.delete_accommodation_btn);
            deleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                dialog.setMessage("Are you sure you want to delete this accommodation?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialogInterface, id) -> {
                            onDelete();
                        })
                        .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
                dialog.create().show();
            });

        }
        else  {
            view = inflater.inflate(R.layout.fragment_accommodation_details, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        //set attributes
    }

    private void getData() {
        Log.d("OpenDoors", "Getting accommodation details: " + dto.getId() + ", " + dto.getAccommodationId());
        if (dto.getId() > 0) {
            //get pending accommodation
            Call<PendingAccommodationWhole> call = ClientUtils.pendingAccommodationService.getById(dto.getId());
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received pending accommodation: " + dto.getId());
                        accommodation = response.body();
                    } else {
                        Log.d("OpenDoors","Message received: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });

        } else {
            //get active accommodation
            Call<PendingAccommodationWhole> call = ClientUtils.accommodationService.getById(dto.getAccommodationId());
            call.enqueue(new Callback<PendingAccommodationWhole>() {
                @Override
                public void onResponse(Call<PendingAccommodationWhole> call, Response<PendingAccommodationWhole> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Received accommodation: " + dto.getAccommodationId());
                        accommodation = response.body();
                    } else {
                        Log.d("OpenDoors","Message received: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<PendingAccommodationWhole> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }

        //change the dto the getting the active accommodation with the total price as needed
    }

    private void onDelete() {
        Log.d("OpenDoors", "Delete accommodation " + dto.getAccommodationId());
        Call<ResponseBody> call = ClientUtils.accommodationService.delete(dto.getAccommodationId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(requireActivity(), "Deleted accommodation", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    navController.popBackStack();
                } else {
                    Log.d("OpenDoors", "Message received: " + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessage = jsonObject.optString("message", "Unknown error");
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });
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