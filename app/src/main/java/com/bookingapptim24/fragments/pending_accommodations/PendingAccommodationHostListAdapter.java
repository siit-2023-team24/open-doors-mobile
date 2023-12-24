package com.bookingapptim24.fragments.pending_accommodations;

import android.app.Activity;
import android.content.res.Resources;
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

import com.bookingapptim24.R;
import com.bookingapptim24.models.AccommodationHost;
import com.bookingapptim24.models.PendingAccommodationHost;

import java.util.ArrayList;

public class PendingAccommodationHostListAdapter extends ArrayAdapter<PendingAccommodationHost> {

    private ArrayList<PendingAccommodationHost> accommodations;

    private Activity activity;

    private FragmentManager fragmentManager;

    public PendingAccommodationHostListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<PendingAccommodationHost> accommodations) {
        super(context, R.layout.pending_accommodation_host_item, accommodations);
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

            accommodationName.setText(accommodation.getName() + " #" + accommodation.getAccommodationId());

            accommodationCard.setOnClickListener(v -> {
                Log.i("OpenDoors", "Clicked: " + accommodation.getName() + ", id: " + accommodation.getId());
                //todo
            });
        }

        return convertView;
    }

}
