package com.bookingapptim24;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookingapptim24.models.AccommodationSearchDTO;

import java.util.List;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.ViewHolder> {

    private List<AccommodationSearchDTO> accommodationList;
    private Context context;

    public AccommodationAdapter(List<AccommodationSearchDTO> accommodationList, Context context) {
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
        holder.ratingTextView.setText(String.valueOf(accommodation.getAverageRating()));
        holder.priceTextView.setText(String.valueOf(accommodation.getPrice()));

        holder.heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.heartImage.setImageResource(R.drawable.clicked_heart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accommodationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView ratingTextView;
        TextView priceTextView;
        ImageView heartImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.accommodationImage);
            nameTextView = itemView.findViewById(R.id.accommodationName);
            ratingTextView = itemView.findViewById(R.id.accommodationRating);
            priceTextView = itemView.findViewById(R.id.accommodationPrice);
            heartImage = itemView.findViewById(R.id.heartImage);
        }
    }
}
