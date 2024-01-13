package com.bookingapptim24.fragments.pending_reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapptim24.R;
import com.bookingapptim24.fragments.FragmentTransition;

public class PendingReviewListPageFragment extends Fragment {


    public PendingReviewListPageFragment() {
    }

   public static PendingReviewListPageFragment newInstance() {
       return new PendingReviewListPageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("OpenDoors", "onCreateView Pending Review List Page");
        return inflater.inflate(R.layout.fragment_pending_review_list_page, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransition.to(PendingReviewListFragment.newInstance(), requireActivity(), false, R.id.pending_review_list);
    }
}