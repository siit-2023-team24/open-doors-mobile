package com.bookingapptim24.fragments.pending_accommodations;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.PendingAccommodationHost;

import java.util.ArrayList;

public class PendingAccommodationHostListAdapter extends ArrayAdapter<PendingAccommodationHost> {

    private ArrayList<PendingAccommodationHost> accommodations;

    private Activity activity;

    private FragmentManager fragmentManager;

    private SessionManager sessionManager;

    public PendingAccommodationHostListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<PendingAccommodationHost> accommodations) {
        super(context, R.layout.pending_accommodation_host_item, accommodations);
        this.accommodations = accommodations;
        activity = context;
        this.fragmentManager = fragmentManager;
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    @Nullable
    @Override
    public PendingAccommodationHost getItem(int position) {
        return accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PendingAccommodationHost accommodation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pending_accommodation_host_item, parent, false);
        }
        LinearLayout accommodationCard = convertView.findViewById(R.id.pending_accommodation_host_card_item);
        ImageView imageView = convertView.findViewById(R.id.accommodationImage);
        TextView accommodationName = convertView.findViewById(R.id.accommodationName);

        if (accommodation != null) {

//            String uri = "@drawable/" + accommodation.getId();
//            Resources resources = getContext().getResources();
//            final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
            imageView.setImageResource(R.drawable.accommodation_image);

            accommodationName.setText(accommodation.getName());

            accommodationCard.setOnClickListener(v -> {
                Log.i("OpenDoors", "Clicked: " + accommodation.getName() + ", id: " + accommodation.getId());
                Bundle args = new Bundle();
                args.putLong("id", accommodation.getId());
                if (accommodation.getAccommodationId() != null)
                    args.putLong("accommodationId", accommodation.getAccommodationId());
                args.putString("name", accommodation.getName());
                if (accommodation.getImage() != null)
                    args.putLong("image", accommodation.getImage());
                NavController navController = Navigation.findNavController(activity, R.id.fragment_nav_content_main);

                String role = sessionManager.getRole();

                if (role.equals("ROLE_ADMIN")) {
                    navController.navigate(R.id.nav_accommodation_details_admin, args);
                } else if (role.equals("ROLE_HOST")) {
                    navController.navigate(R.id.nav_accommodation_details_host, args);
                }
                else {
                    navController.navigate(R.id.nav_accommodation_details, args);
                }
            });
        }

        return convertView;
    }

}
