package com.bookingapptim24.fragments.reservation_list;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.ReservationRequestForGuest;
import com.bookingapptim24.models.enums.ReservationRequestStatus;
import com.bookingapptim24.util.DataChangesListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRequestForGuestListAdapter extends ArrayAdapter<ReservationRequestForGuest> {

    private ArrayList<ReservationRequestForGuest> requests;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private DataChangesListener listener;

    public ReservationRequestForGuestListAdapter(@NonNull Activity context, FragmentManager fragmentManager, ArrayList<ReservationRequestForGuest> requests) {
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
    public ReservationRequestForGuest getItem(int position) {
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
        ReservationRequestForGuest request = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_request_guest_item, parent, false);

        TextView requestId = convertView.findViewById(R.id.request_id);
        TextView accommodation = convertView.findViewById(R.id.request_accommodation_name);
        TextView dateRange = convertView.findViewById(R.id.request_date_range);
        TextView status = convertView.findViewById(R.id.request_status);
        TextView guestNumber = convertView.findViewById(R.id.request_guest_number);
        TextView totalPrice = convertView.findViewById(R.id.request_total_price);
        TextView timestamp = convertView.findViewById(R.id.request_timestamp);
        ImageView imageView = convertView.findViewById(R.id.request_image);

        Button deleteBtn = convertView.findViewById(R.id.request_delete_button);
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to delete this request?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDelete(request);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button cancelBtn = convertView.findViewById(R.id.request_cancel_button);
        cancelBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to cancel this reservation?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onCancel(request);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        if (request != null) {
            requestId.setText("Request ID: " + String.valueOf(request.getId()));
            if (request.getAccommodationName() != null && !request.getAccommodationName().isEmpty())
                accommodation.setText(request.getAccommodationName());
            status.setText("Status: " + request.getStatus().getStatus());
            guestNumber.setText("Number of guests: " + String.valueOf(request.getGuestNumber()));
            totalPrice.setText("Total price: " + String.valueOf(request.getTotalPrice()));
            dateRange.setText(sdf.format(request.getStartDate()) + " - " + sdf.format(request.getEndDate()));
            timestamp.setText(sdf.format(request.getTimestamp()));
            if (request.getStatus().equals(ReservationRequestStatus.PENDING))
                deleteBtn.setVisibility(View.VISIBLE);
            else
                deleteBtn.setVisibility(View.GONE);

            if (request.getStatus().equals(ReservationRequestStatus.CONFIRMED) && request.getStartDate().after(new Timestamp(System.currentTimeMillis())))
                cancelBtn.setVisibility(View.VISIBLE);
            else
                cancelBtn.setVisibility(View.GONE);

            if (request.getImageId() != null && request.getImageId() > 0) {
                imageView.setVisibility(View.VISIBLE);
                loadImage(imageView, request.getImageId());
            }
        }
        return convertView;
    }

    private void onCancel(ReservationRequestForGuest request) {
        Log.d("OpenDoors", "Deny request " + request.getId());
        Call<ResponseBody> call = ClientUtils.reservationRequestService.cancel(request.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Reservation canceled", Toast.LENGTH_SHORT).show();
                    listener.onDataChanged();
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

    private void onDelete(ReservationRequestForGuest request) {
        Log.d("OpenDoors", "Deny request " + request.getId());
        Call<ResponseBody> call = ClientUtils.reservationRequestService.delete(request.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Request deleted", Toast.LENGTH_SHORT).show();
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

    private void loadImage(ImageView image, Long id) {
        Call<ResponseBody> getImage = ClientUtils.imageService.getById(id, false);
        getImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        File imageFile = createTempImageFile(bitmap);
                        Picasso.get().load(Uri.fromFile(imageFile)).into(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else Log.e("OpenDoors", "Error getting image");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OpenDoors", "Error loading image");
                image.setImageResource(R.drawable.accommodation_image);
            }
        });
    }

    private File createTempImageFile(Bitmap bitmap) throws IOException {
        File tempDir = getContext().getCacheDir();
        File tempFile = File.createTempFile("image", "png", tempDir);

        OutputStream os = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();

        return tempFile;
    }
}
