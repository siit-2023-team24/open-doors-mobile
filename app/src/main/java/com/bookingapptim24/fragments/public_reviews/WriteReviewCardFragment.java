package com.bookingapptim24.fragments.public_reviews;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentWriteReviewCardBinding;
import com.bookingapptim24.models.enums.NotificationType;
import com.bookingapptim24.models.reviews.AccommodationReviewWhole;
import com.bookingapptim24.models.reviews.HostReviewWhole;
import com.bookingapptim24.models.reviews.NewReview;
import com.bookingapptim24.util.DataChangesListener;
import com.bookingapptim24.util.SocketService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReviewCardFragment extends Fragment {
    private FragmentWriteReviewCardBinding binding;
    private DataChangesListener listener;
    private int rating = 0;
    private Long recipientId;
    private String hostUsername;

    private boolean isHost;

    private static final String IS_HOST = "isHost";
    private static final String RECIPIENT_ID = "recipientId";
    private static final String HOST_USERNAME = "hostUsername";

    public static WriteReviewCardFragment newInstance(Long recipientId, boolean isHost, String hostUsername) {
        WriteReviewCardFragment fragment = new WriteReviewCardFragment();
        Bundle args = new Bundle();
        args.putLong(RECIPIENT_ID, recipientId);
        args.putBoolean(IS_HOST, isHost);
        args.putString(HOST_USERNAME, hostUsername);
        fragment.setArguments(args);
        return fragment;
    }

    public WriteReviewCardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWriteReviewCardBinding.inflate(inflater, container, false);
        binding.errorTextView.setVisibility(View.INVISIBLE);
        recipientId = getArguments() != null ? getArguments().getLong(RECIPIENT_ID) : null;
        isHost = getArguments() != null ? getArguments().getBoolean(IS_HOST) : false;
        hostUsername = getArguments() != null ? getArguments().getString(HOST_USERNAME) : "";
        binding.starContainer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(1);
            }
        });

        binding.starContainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(2);
            }
        });

        binding.starContainer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(3);
            }
        });

        binding.starContainer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(4);
            }
        });

        binding.starContainer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRating(5);
            }
        });

        binding.reviewButton.setOnClickListener(v -> {
            if (rating==0) {
                binding.errorTextView.setVisibility(View.VISIBLE);
                return;
            }
            binding.errorTextView.setVisibility(View.INVISIBLE);
            SessionManager sessionManager = new SessionManager(requireContext());
            NewReview dto = new NewReview(
                    rating,
                    binding.commentEditText.getText().toString().trim(),
                    sessionManager.getUserId(),
                    recipientId
            );
            if (isHost) {
                saveHostReview(dto);
            } else {
                saveAccommodationReview(dto);
            }
        });
        return binding.getRoot();
    }

    private void updateRating(int rating) {
        this.rating = rating;
        binding.rating.setText(String.valueOf(rating));
        updateStarVisibility(rating);
    }

    private void updateStarVisibility(int rating) {
        for (int i = 1; i <= 5; i++) {
            int emptyStarId = getResources().getIdentifier("emptyStar" + i, "id", requireActivity().getPackageName());
            int fullStarId = getResources().getIdentifier("fullStar" + i, "id", requireActivity().getPackageName());

            ImageView emptyStar = requireView().findViewById(emptyStarId);
            ImageView fullStar = requireView().findViewById(fullStarId);
//            TODO: generate and use star borders

            if (i <= rating) {
                emptyStar.setVisibility(View.GONE);
                fullStar.setVisibility(View.VISIBLE);
            } else {
                emptyStar.setVisibility(View.VISIBLE);
                fullStar.setVisibility(View.GONE);
            }
        }
    }

    private void saveAccommodationReview(NewReview dto) {
        Call<AccommodationReviewWhole> call = ClientUtils.accommodationReviewService.createAccommodationReview(dto);
        call.enqueue(new Callback<AccommodationReviewWhole>() {
            @Override
            public void onResponse(Call<AccommodationReviewWhole> call, Response<AccommodationReviewWhole> response) {
                if (response.isSuccessful()){
                    Log.d("Response", response.message());
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
                listener.onDataChanged();
            }
            @Override
            public void onFailure(Call<AccommodationReviewWhole> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void saveHostReview(NewReview dto) {
        Call<HostReviewWhole> call = ClientUtils.hostReviewService.createHostReview(dto);
        call.enqueue(new Callback<HostReviewWhole>() {
            @Override
            public void onResponse(Call<HostReviewWhole> call, Response<HostReviewWhole> response) {
                if (response.isSuccessful()){
                    Log.d("Response", response.message());

                    sendNotification(hostUsername, "You have a new review",
                            NotificationType.HOST_REVIEW.getTypeMessage());
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
                listener.onDataChanged();
            }
            @Override
            public void onFailure(Call<HostReviewWhole> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    public void setListener(DataChangesListener listener) {
        this.listener = listener;
    }

    private void sendNotification(String username, String message, String type) {
        Intent intent = new Intent(requireActivity(), SocketService.class);
        intent.putExtra("username", username);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        intent.setAction(SocketService.ACTION_SEND_NOTIFICATION);
        requireActivity().startForegroundService(intent);
    }

}