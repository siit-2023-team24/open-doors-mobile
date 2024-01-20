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

import com.bookingapptim24.Accommodation;
import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentAccommodationPublicReviewsBinding;
import com.bookingapptim24.databinding.FragmentWriteReportCardListBinding;
import com.bookingapptim24.fragments.user_reports.WriteReportCardAdapter;
import com.bookingapptim24.fragments.user_reports.WriteReportCardListFragment;
import com.bookingapptim24.models.reviews.AccommodationReviews;
import com.bookingapptim24.models.reviews.ReviewDetails;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationPublicReviewsFragment extends ListFragment implements DataChangesListener {
    private PublicReviewAdapter adapter;
    private SessionManager sessionManager;
    private FragmentAccommodationPublicReviewsBinding binding;
    private List<ReviewDetails> reviews = new ArrayList<>();

    private Long accommodationId;
    private static final String ACCOMMODATION_ID = "accommodationId";
    public AccommodationPublicReviewsFragment() {}

    public static AccommodationPublicReviewsFragment newInstance(Long accommodationId) {
        AccommodationPublicReviewsFragment fragment = new AccommodationPublicReviewsFragment();
        Bundle args = new Bundle();
        args.putLong(ACCOMMODATION_ID, accommodationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationPublicReviewsBinding.inflate(inflater, container, false);
        accommodationId = getArguments() != null ? getArguments().getLong(ACCOMMODATION_ID) : null;
        sessionManager = new SessionManager(requireContext());
        adapter = new PublicReviewAdapter(getActivity(), getActivity().getSupportFragmentManager(), reviews, false, false);
        adapter.setListener(AccommodationPublicReviewsFragment.this);
        setListAdapter(adapter);

        if (savedInstanceState == null) {
            WriteReviewCardFragment innerFragment = WriteReviewCardFragment.newInstance(accommodationId, false);
            innerFragment.setListener(AccommodationPublicReviewsFragment.this);
            getChildFragmentManager().beginTransaction()
                    .replace(binding.writeAccommodationReview.getId(), innerFragment)
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
        Call<AccommodationReviews> call = ClientUtils.accommodationReviewService.getReviewsForAccommodation(
                accommodationId,
                guestId
        );
        call.enqueue(new Callback<AccommodationReviews>() {
            @Override
            public void onResponse(Call<AccommodationReviews> call, Response<AccommodationReviews> response) {
                if (response.isSuccessful()){
                    reviews.clear();
                    adapter.setHasPending(false);
                    binding.noReviewsText.setVisibility(View.GONE);
                    binding.writeAccommodationReview.setVisibility(View.GONE);
                    binding.averageRatingContainer.setVisibility(View.GONE);
                    AccommodationReviews reviewDTOs = response.body();
                    if(reviewDTOs.getReviews().isEmpty()) {
                        binding.noReviewsText.setVisibility(View.VISIBLE);
                    } else {
                        binding.averageRatingContainer.setVisibility(View.VISIBLE);
                        binding.averageRating.setText(String.valueOf(reviewDTOs.getAverageRating()));
                    }
                    if (reviewDTOs.getUnapprovedReview()!=null) {
                        reviews.add(reviewDTOs.getUnapprovedReview());
                        adapter.setHasPending(true);
                    }
                    if(reviewDTOs.getIsReviewable()) {
                        binding.writeAccommodationReview.setVisibility(View.VISIBLE);
                    }
                    reviews.addAll(reviewDTOs.getReviews());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<AccommodationReviews> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onDataChanged() {
        getData();
    }
}