package com.bookingapptim24.additionalAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookingapptim24.R;

import java.util.List;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.ImageViewHolder> {

    private List<Bitmap> bitmaps;
    private Context context;

    public BitmapAdapter(Context context, List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        this.context = context;
    }

    @NonNull
    @Override
    public BitmapAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image, parent, false);
        return new BitmapAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BitmapAdapter.ImageViewHolder holder, int position) {
        Bitmap bitmap = bitmaps.get(position);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }


    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
