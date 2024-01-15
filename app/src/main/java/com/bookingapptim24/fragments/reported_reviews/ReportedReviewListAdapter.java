package com.bookingapptim24.fragments.reported_reviews;

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
import com.bookingapptim24.models.ReportedReview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedReviewListAdapter extends ArrayAdapter<ReportedReview> {

    private ArrayList<ReportedReview> reviews;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    public ReportedReviewListAdapter(@NonNull Activity context, FragmentManager fragmentManager,  ArrayList<ReportedReview> reviews) {
        super(context, R.layout.fragment_reported_review_list, reviews);
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
    public ReportedReview getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportedReview review = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reported_review_item, parent, false);

        TextView authorUsername = convertView.findViewById(R.id.review_item_author_username);
        TextView timestamp = convertView.findViewById(R.id.review_item_timestamp);
        TextView rating = convertView.findViewById(R.id.review_item_rating);
        TextView comment = convertView.findViewById(R.id.review_item_comment);
        TextView hostUsername = convertView.findViewById(R.id.review_item_host_username);

        Button deleteBtn = convertView.findViewById(R.id.delete_review_btn);
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to delete this review?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDelete(review);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button dismissBtn = convertView.findViewById(R.id.dismiss_review_btn);
        dismissBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to dismiss this report?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDismiss(review);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        if (review != null) {
            authorUsername.setText(review.getAuthorUsername());
            timestamp.setText(sdf.format(review.getTimestamp()));
            rating.setText(String.valueOf(review.getRating()));
            comment.setText(review.getComment());
            hostUsername.setText(review.getHostUsername());
        }
        return convertView;
    }

    private void onDelete(ReportedReview review) {
        Log.d("OpenDoors", "Delete pending review " + review.getId());
        Call<ResponseBody> call = ClientUtils.hostReviewService.delete(review.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Review deleted", Toast.LENGTH_SHORT).show();
                    reviews.remove(review);
                    ReportedReviewListAdapter.this.notifyDataSetChanged();
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

    private void onDismiss(ReportedReview review) {
        Log.d("OpenDoors", "Dismiss pending review " + review.getId());
        Call<ResponseBody> call = ClientUtils.hostReviewService.dismiss(review.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Report dismissed", Toast.LENGTH_SHORT).show();
                    reviews.remove(review);
                    ReportedReviewListAdapter.this.notifyDataSetChanged();
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
