package com.bookingapptim24.fragments;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransition {
    public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int layoutViewID)
    {
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(layoutViewID, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);

        Log.d("FragmentTransition", "Fragment transition committed for " + newFragment.getClass().getSimpleName());

        transaction.commit();
    }
}
