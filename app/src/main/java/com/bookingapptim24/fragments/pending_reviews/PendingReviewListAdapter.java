package com.bookingapptim24.fragments.pending_reviews;

import android.app.Activity;
import android.content.Intent;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.PendingReview;
import com.bookingapptim24.models.enums.NotificationType;
import com.bookingapptim24.util.SocketService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingReviewListAdapter extends ArrayAdapter<PendingReview> {

    private ArrayList<PendingReview> reviews;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    public PendingReviewListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<PendingReview> reviews) {
        super(context, R.layout.fragment_pending_review_list, reviews);
        this.reviews = reviews;
        this.activity = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Nullable
    @Override
    public PendingReview getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PendingReview review = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pending_review_item, parent, false);

        TextView authorUsername = convertView.findViewById(R.id.review_item_author_username);
        TextView timestamp = convertView.findViewById(R.id.review_item_timestamp);
        TextView rating = convertView.findViewById(R.id.review_item_rating);
        TextView comment = convertView.findViewById(R.id.review_item_comment);
        TextView accommodationName = convertView.findViewById(R.id.review_item_accommodation_name);

        Button approveBtn = convertView.findViewById(R.id.approve_review_btn);
        approveBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to approve this review?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onApprove(review);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();

        });

        Button denyBtn = convertView.findViewById(R.id.deny_review_btn);
        denyBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to deny this review?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDeny(review);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        if (review != null) {
            authorUsername.setText(review.getAuthorUsername());
            timestamp.setText(sdf.format(review.getTimestamp()));
            rating.setText(String.valueOf(review.getRating()));
            comment.setText(review.getComment());
            accommodationName.setText(review.getAccommodationName());
        }
        return convertView;
    }

    private void onApprove(PendingReview review) {
        Log.d("OpenDoors", "Approve pending review " + review.getId());
        Call<ResponseBody> call = ClientUtils.accommodationReviewService.approve(review.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Review approved", Toast.LENGTH_SHORT).show();
                    reviews.remove(review);
                    PendingReviewListAdapter.this.notifyDataSetChanged();


                    sendNotification(review.getHostUsername(), review.getAccommodationName() + " has been reviewed",
                            NotificationType.ACCOMMODATION_REVIEW.getTypeMessage());
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

    private void onDeny(PendingReview review) {
        Log.d("OpenDoors", "Deny pending review " + review.getId());
        Call<ResponseBody> call = ClientUtils.accommodationReviewService.deny(review.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Review denied", Toast.LENGTH_SHORT).show();
                    reviews.remove(review);
                    notifyDataSetChanged();
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

    private void sendNotification(String username, String message, String type) {
        Intent intent = new Intent(activity, SocketService.class);
        intent.putExtra("username", username);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        intent.setAction(SocketService.ACTION_SEND_NOTIFICATION);
        activity.startForegroundService(intent);
    }
}