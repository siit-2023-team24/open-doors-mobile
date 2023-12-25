package com.bookingapptim24.fragments.accommodation_host;

import android.app.Activity;
import android.content.res.Resources;
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
import com.bookingapptim24.models.AccommodationHost;

import java.util.ArrayList;

public class AccommodationHostListAdapter extends ArrayAdapter<AccommodationHost> {

    private ArrayList<AccommodationHost> accommodations;

    private Activity activity;

    private FragmentManager fragmentManager;

    public AccommodationHostListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<AccommodationHost> accommodations) {
        super(context, R.layout.accommodation_host_item, accommodations);
        this.accommodations = accommodations;
        activity = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    @Nullable
    @Override
    public AccommodationHost getItem(int position) {
        return accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationHost accommodation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_host_item, parent, false);
        }
        LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_host_card_item);
        ImageView imageView = convertView.findViewById(R.id.accommodationImage);
        TextView accommodationName = convertView.findViewById(R.id.accommodationName);

        if (accommodation != null) {

//            String uri = "@drawable/accommodation_image.jpg"; // + accommodation.getId();
//            Resources resources = getContext().getResources();
//            final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
            imageView.setImageResource(R.drawable.accommodation_image);

            accommodationName.setText(accommodation.getName());

            accommodationCard.setOnClickListener(v -> {
                Log.i("OpenDoors", "Clicked: " + accommodation.getName() + ", id: " + accommodation.getId());
                Bundle args = new Bundle();
                args.putLong("accommodationId", accommodation.getId());
                args.putString("name", accommodation.getName());
                if (accommodation.getImage() != null)
                    args.putLong("image", accommodation.getImage());
                NavController navController = Navigation.findNavController(activity, R.id.fragment_nav_content_main);

                //todo get role
                String role = "ROLE_HOST";

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
