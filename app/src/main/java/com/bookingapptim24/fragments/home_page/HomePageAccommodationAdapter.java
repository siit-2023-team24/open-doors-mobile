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
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.AccommodationFavorites;
import com.bookingapptim24.models.AccommodationSearchDTO;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageAccommodationAdapter extends RecyclerView.Adapter<HomePageAccommodationAdapter.ViewHolder> {

    private List<AccommodationSearchDTO> accommodationList;
    private Context context;
    private SessionManager sessionManager;
    private View view;

    public HomePageAccommodationAdapter(List<AccommodationSearchDTO> accommodationList, Context context) {
        this.accommodationList = accommodationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.accommodation_item, parent, false);
        sessionManager = new SessionManager(view.getContext());
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

        if(sessionManager.getRole().equals("ROLE_GUEST")) {
            holder.heartImage.setVisibility(View.VISIBLE);
            if (accommodation.isFavoriteForGuest()) {
                holder.heartImage.setImageResource(R.drawable.clicked_heart);
            } else {
                holder.heartImage.setImageResource(R.drawable.heart);
            }

            holder.heartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(accommodation.isFavoriteForGuest()) {
                        holder.heartImage.setImageResource(R.drawable.heart);
                        accommodation.setFavoriteForGuest(false);
                        removeFromFavorites(accommodation.getId());
                    } else {
                        holder.heartImage.setImageResource(R.drawable.clicked_heart);
                        accommodation.setFavoriteForGuest(true);
                        addToFavorites(accommodation.getId());
                    }
                }
            });
        }

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

    private void addToFavorites(Long accommodationId) {
        AccommodationFavorites accommodationFavoritesDTO = new AccommodationFavorites(accommodationId, sessionManager.getUserId());
        Call<ResponseBody> call = ClientUtils.accommodationService.addToFavorites(accommodationFavoritesDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showSnackbar("Added to favorites!");
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                    showSnackbar("Adding to favorites failed!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void removeFromFavorites(Long accommodationId) {
        AccommodationFavorites accommodationFavoritesDTO = new AccommodationFavorites(accommodationId, sessionManager.getUserId());
        Call<ResponseBody> call = ClientUtils.accommodationService.removeFromFavorites(accommodationFavoritesDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showSnackbar("Removed from favorites!");
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                    showSnackbar("Removing from favorites failed!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
