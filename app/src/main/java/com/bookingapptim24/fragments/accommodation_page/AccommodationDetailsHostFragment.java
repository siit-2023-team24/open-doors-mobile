package com.bookingapptim24.fragments.accommodation_page;

import android.content.Intent;
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
import com.bookingapptim24.activities.CreateAccommodationActivity;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.fragments.FragmentTransition;
import com.bookingapptim24.models.AccommodationWithTotalPrice;
import com.bookingapptim24.models.PendingAccommodationHost;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccommodationDetailsHostFragment extends Fragment {

    private SessionManager sessionManager;

    private static final String ARG_ID = "id";
    private static final String ARG_ACCOMMODATION_ID = "accommodationId";

    private PendingAccommodationHost dto = new PendingAccommodationHost();


    public AccommodationDetailsHostFragment() {}

   public static AccommodationDetailsHostFragment newInstance(Long id, Long accommodationId) {
        AccommodationDetailsHostFragment fragment = new AccommodationDetailsHostFragment();
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
        Log.i("OpenDoors", "Accommodation Details Host onCreateView()");
        View view = inflater.inflate(R.layout.fragment_accommodation_details_host, container, false);

        //todo onclick financial report

        Button editBtn = view.findViewById(R.id.edit_accommodation_btn);
        editBtn.setOnClickListener(v -> onEdit());

        Button deleteBtn = view.findViewById(R.id.delete_accommodation_btn);
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
            dialog.setMessage("Are you sure you want to delete this accommodation?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> onDelete())
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });
        FragmentTransition.to(AccommodationDetailsFragment.newInstance(dto.getId(), dto.getAccommodationId()),
                requireActivity(), false, R.id.accommodation_details);
        return view;
    }



    private void onDelete() {
        if (dto.getId() == 0)
            onDeleteActive();
        else
            onDeletePending();
    }

    private void onDeleteActive() {
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

    private void onDeletePending() {
        Log.d("OpenDoors", "Delete pending accommodation " + dto.getId());
        Call<ResponseBody> call = ClientUtils.pendingAccommodationService.delete(dto.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(requireActivity(), "Deleted pending accommodation", Toast.LENGTH_SHORT).show();
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

    private void onEdit() {
        Intent intent = new Intent(requireActivity(), CreateAccommodationActivity.class);
        intent.putExtra("id", dto.getId());
        intent.putExtra("accommodationId", dto.getAccommodationId());
        startActivity(intent);
    }
}