package com.bookingapptim24.fragments.public_reviews;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.reviews.ReviewDetails;
import com.bookingapptim24.util.DataChangesListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicReviewAdapter extends ArrayAdapter<ReviewDetails> {

    private List<ReviewDetails> reviews;
    private Activity activity;
    private FragmentManager fragmentManager;
    private DataChangesListener listener;
    private SessionManager sessionManager;
    private boolean isHost = false;
    private boolean hasPending = false;
    private boolean canReport = false;


    private String viewDate(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat(activity.getString(R.string.date_format));
        return sdf.format(date);
    }

    public PublicReviewAdapter(@NonNull Activity context, FragmentManager fragmentManager, List<ReviewDetails> reviews, boolean canReport, boolean isHost) {
        super(context, R.layout.public_review_card_item, reviews);
        this.reviews = reviews;
        this.sessionManager = new SessionManager(context);
        this.activity = context;
        this.fragmentManager = fragmentManager;
        this.canReport = canReport;
        this.isHost = isHost;
    }

    public void setListener(DataChangesListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Nullable
    @Override
    public ReviewDetails getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewDetails review = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.public_review_card_item, parent, false);

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView commentTextView = convertView.findViewById(R.id.commentTextView);
        TextView ratingTextView = convertView.findViewById(R.id.ratingTextView);
        TextView timestampTextView = convertView.findViewById(R.id.timestampTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button reportButton = convertView.findViewById(R.id.reportButton);
        Button withdrawButton = convertView.findViewById(R.id.withdrawButton);
        TextView pendingTextView = convertView.findViewById(R.id.pending_text_view);
        if (review != null) {
            usernameTextView.setText(review.getAuthorUsername());
            commentTextView.setText(review.getComment());
            ratingTextView.setText(String.valueOf(review.getRating()));
            timestampTextView.setText(viewDate(review.getTimestamp()));
            deleteButton.setVisibility(View.GONE);
            reportButton.setVisibility(View.GONE);
            withdrawButton.setVisibility(View.GONE);
            pendingTextView.setVisibility(View.GONE);
            if(sessionManager.isLoggedIn() && sessionManager.getUsername().equals(review.getAuthorUsername())) {
                deleteButton.setVisibility(View.VISIBLE);
            }
            if (canReport && !review.getIsProcessed()) {
                reportButton.setVisibility(View.VISIBLE);
            }
            if (canReport && review.getIsProcessed()) {
                withdrawButton.setVisibility(View.VISIBLE);
            }

            reportButton.setOnClickListener(v -> {
                changeReportStatus(review.getId());
            });

            withdrawButton.setOnClickListener(v -> {
                changeReportStatus(review.getId());
            });

            if(sessionManager.isLoggedIn() && hasPending && review.getAuthorUsername().equals(sessionManager.getUsername())) {
                pendingTextView.setVisibility(View.VISIBLE);
            }
        }

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage(activity.getString(R.string.dialog_delete_review))
                    .setCancelable(false)
                    .setPositiveButton(activity.getString(R.string.yes), (dialogInterface, id) -> {
                        if (isHost) {
                            Call<ResponseBody> call = ClientUtils.hostReviewService.delete(review.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("Response", response.message());
                                        listener.onDataChanged();
                                    } else {
                                        Log.d("REZ", "Message received: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                                }
                            });
                        } else {
                            Call<ResponseBody> call = ClientUtils.accommodationReviewService.deleteAccommodationReview(review.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("Response", response.message());
                                        listener.onDataChanged();
                                    } else {
                                        Log.d("REZ", "Message received: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                                }
                            });
                        }
                    })
                    .setNegativeButton(activity.getString(R.string.no), (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        return convertView;
    }

    private void changeReportStatus(Long id) {
        Call<ResponseBody> call = ClientUtils.hostReviewService.changeReportedStatus(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", response.message());
                    listener.onDataChanged();
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    public void setHasPending(boolean hasPending) {
        this.hasPending = hasPending;
    }
}
