package com.bookingapptim24.fragments.reservation_list;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.ReservationRequestForHost;
import com.bookingapptim24.models.enums.ReservationRequestStatus;
import com.bookingapptim24.util.DataChangesListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRequestForHostListAdapter extends ArrayAdapter<ReservationRequestForHost> {

    private ArrayList<ReservationRequestForHost> requests;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private DataChangesListener listener;

    public ReservationRequestForHostListAdapter(@NonNull Activity context, FragmentManager fragmentManager, ArrayList<ReservationRequestForHost> requests) {
        super(context, R.layout.fragment_reservation_request_for_host_list, requests);
        this.requests = requests;
        this.activity = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Nullable
    @Override
    public ReservationRequestForHost getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setListener(DataChangesListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationRequestForHost request = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_request_host_item, parent, false);

        TextView requestId = convertView.findViewById(R.id.request_id);
        TextView accommodation = convertView.findViewById(R.id.request_accommodation_name);
        TextView dateRange = convertView.findViewById(R.id.request_date_range);
        TextView status = convertView.findViewById(R.id.request_status);
        TextView guestUsername = convertView.findViewById(R.id.request_guest_username);
        TextView cancelledNumber = convertView.findViewById(R.id.request_cancelled_number);
        TextView guestNumber = convertView.findViewById(R.id.request_guest_number);
        TextView totalPrice = convertView.findViewById(R.id.request_total_price);

        Button denyBtn = convertView.findViewById(R.id.request_deny_button);
        denyBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to deny this request?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDeny(request);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button confirm = convertView.findViewById(R.id.request_confirm_button);
        confirm.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to confirm this request?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onConfirm(request);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        if (request != null) {
            requestId.setText("Request ID: " + String.valueOf(request.getId()));
            if (request.getAccommodationName() != null && !request.getAccommodationName().isEmpty())
                accommodation.setText(request.getAccommodationName());
            status.setText("Status: " + request.getStatus().getStatus());
            guestUsername.setText("Guest: " + request.getGuestUsername());
            cancelledNumber.setText("The guest has cancelled " + String.valueOf(request.getCancelledNumber() + " time(s)."));
            guestNumber.setText("Number of guests: " + String.valueOf(request.getGuestNumber()));
            totalPrice.setText("Total price: " + String.valueOf(request.getTotalPrice()));
            dateRange.setText(sdf.format(request.getStartDate()) + " - " + sdf.format(request.getEndDate()));
            if (request.getStatus().equals(ReservationRequestStatus.PENDING)) {
                denyBtn.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
            } else {
                denyBtn.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private void onConfirm(ReservationRequestForHost request) {
        Log.d("OpenDoors", "Confirm request " + request.getId());
        Call<ResponseBody> call = ClientUtils.reservationRequestService.confirm(request.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Request confirmed", Toast.LENGTH_SHORT).show();
                    listener.onDataChanged();
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

    private void onDeny(ReservationRequestForHost request) {
        Log.d("OpenDoors", "Deny request " + request.getId());
        Call<ResponseBody> call = ClientUtils.reservationRequestService.deny(request.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Request denied", Toast.LENGTH_SHORT).show();
                    listener.onDataChanged();
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
