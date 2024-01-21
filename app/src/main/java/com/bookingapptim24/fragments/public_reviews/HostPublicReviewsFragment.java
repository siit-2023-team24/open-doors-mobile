package com.bookingapptim24.fragments.public_reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentAccommodationPublicReviewsBinding;
import com.bookingapptim24.databinding.FragmentHostPublicReviewsBinding;
import com.bookingapptim24.models.reviews.AccommodationReviews;
import com.bookingapptim24.models.reviews.HostPublicData;
import com.bookingapptim24.models.reviews.ReviewDetails;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostPublicReviewsFragment extends ListFragment implements DataChangesListener {
    private PublicReviewAdapter adapter;
    private SessionManager sessionManager;
    private FragmentHostPublicReviewsBinding binding;
    private List<ReviewDetails> reviews = new ArrayList<>();
    private Long hostId;
    private String hostUsername;
    private static final String HOST_ID = "hostId";
    private static final String HOST_USERNAME = "hostUsername";
    public HostPublicReviewsFragment() {}

    public static HostPublicReviewsFragment newInstance(Long hostId, String hostUsername) {
        HostPublicReviewsFragment fragment = new HostPublicReviewsFragment();
        Bundle args = new Bundle();
        args.putLong(HOST_ID, hostId);
        args.putString(HOST_USERNAME, hostUsername);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        binding = FragmentHostPublicReviewsBinding.inflate(inflater, container, false);
        hostId = getArguments() != null ? getArguments().getLong(HOST_ID) : sessionManager.getUserId();
        hostUsername = getArguments() != null ? getArguments().getString(HOST_USERNAME) : "";
        boolean canReport = sessionManager.isLoggedIn() && sessionManager.getUserId() == hostId;
        adapter = new PublicReviewAdapter(getActivity(), getActivity().getSupportFragmentManager(), reviews, canReport, true);
        adapter.setListener(HostPublicReviewsFragment.this);
        setListAdapter(adapter);

        if (savedInstanceState == null) {
            WriteReviewCardFragment innerFragment = WriteReviewCardFragment.newInstance(hostId, true, hostUsername);
            innerFragment.setListener(HostPublicReviewsFragment.this);
            getChildFragmentManager().beginTransaction()
                    .replace(binding.writeHostReview.getId(), innerFragment)
                    .commit();
        }

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getListView().setDividerHeight(2);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getData() {
        Long guestId = 0L;
        if (sessionManager.isLoggedIn() && sessionManager.getRole().equals("ROLE_GUEST")) guestId = sessionManager.getUserId();
        Call<HostPublicData> call = ClientUtils.hostReviewService.getReviewsForHost(
                hostId,
                guestId
        );
        call.enqueue(new Callback<HostPublicData>() {
            @Override
            public void onResponse(Call<HostPublicData> call, Response<HostPublicData> response) {
                if (response.isSuccessful()){
                    reviews.clear();
                    binding.noReviewsText.setVisibility(View.GONE);
                    binding.writeHostReview.setVisibility(View.GONE);
                    binding.averageRatingContainer.setVisibility(View.GONE);
                    HostPublicData reviewDTOs = response.body();
                    binding.hostEmailText.setText(reviewDTOs.getUsername());
                    binding.hostNameText.setText(reviewDTOs.getFirstName()+ " "+reviewDTOs.getLastName());
                    if(reviewDTOs.getReviews().isEmpty()) {
                        binding.noReviewsText.setVisibility(View.VISIBLE);
                    } else {
                        binding.averageRatingContainer.setVisibility(View.VISIBLE);
                        binding.averageRating.setText(String.valueOf(reviewDTOs.getAverageRating()));
                    }
                    if(reviewDTOs.getIsReviewable()) {
                        binding.writeHostReview.setVisibility(View.VISIBLE);
                    }
                    reviews.addAll(reviewDTOs.getReviews());
                    adapter.notifyDataSetChanged();
                    Log.d("REZ","I did a thing");
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<HostPublicData> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onDataChanged() {
        getData();
    }

}