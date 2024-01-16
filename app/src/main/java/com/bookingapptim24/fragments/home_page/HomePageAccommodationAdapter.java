package com.bookingapptim24.fragments.home_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bookingapptim24.R;
import com.bookingapptim24.models.AccommodationSearchDTO;

import java.util.List;

public class HomePageAccommodationAdapter extends RecyclerView.Adapter<HomePageAccommodationAdapter.ViewHolder> {

    private List<AccommodationSearchDTO> accommodationList;
    private Context context;
    public HomePageAccommodationAdapter(List<AccommodationSearchDTO> accommodationList, Context context) {
        this.accommodationList = accommodationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accommodation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AccommodationSearchDTO accommodation = accommodationList.get(position);
//        holder.imageView.setImageResource(accommodation.getImageResource());
        holder.nameTextView.setText(accommodation.getName());
        if(accommodation.getAverageRating() != null) {
            holder.ratingStarView.setVisibility(View.VISIBLE);
            holder.ratingTextView.setText(String.valueOf(accommodation.getAverageRating()));
            holder.ratingTextView.setVisibility(View.VISIBLE);
        } else {
            holder.notRatedTextView.setVisibility(View.VISIBLE);
        }
        if(accommodation.getTotalPrice() != null && accommodation.getTotalPrice() != 0.0) {
            holder.totalPriceTextView.setText(String.valueOf(accommodation.getTotalPrice()) + " rsd total");
            holder.totalPriceTextView.setVisibility(View.VISIBLE);
        }
        if(accommodation.isPricePerGuest()) {
            holder.priceTextView.setText(String.valueOf(accommodation.getPrice()) + " rsd/guest");
        } else {
            holder.priceTextView.setText(String.valueOf(accommodation.getPrice()) + " rsd/night");
        }

        holder.heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.heartImage.setImageResource(R.drawable.clicked_heart);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.openAccommodationDetailsFragment(accommodation);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(accommodationList == null) return 0;
        return accommodationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        ImageView ratingStarView;
        TextView ratingTextView;
        TextView priceTextView;
        TextView totalPriceTextView;
        ImageView heartImage;
        TextView notRatedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.accommodationImage);
            nameTextView = itemView.findViewById(R.id.accommodationName);
            ratingStarView = itemView.findViewById(R.id.ratingStar);
            ratingTextView = itemView.findViewById(R.id.accommodationRating);
            priceTextView = itemView.findViewById(R.id.accommodationPrice);
            totalPriceTextView = itemView.findViewById(R.id.accommodationPriceTotal);
            heartImage = itemView.findViewById(R.id.heartImage);
            notRatedTextView = itemView.findViewById(R.id.notRated);
        }

        private void openAccommodationDetailsFragment(AccommodationSearchDTO accommodation) {
            Bundle args = new Bundle();
            args.putLong("accommodationId", accommodation.getId());

            NavController navController = Navigation.findNavController((Activity) context, R.id.fragment_nav_content_main);
            navController.navigate(R.id.nav_accommodation_details, args);
        }
    }
}
