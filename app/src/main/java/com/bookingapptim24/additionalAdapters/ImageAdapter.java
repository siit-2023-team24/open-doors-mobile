package com.bookingapptim24.additionalAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Long> imageIds;
    private Context context;

    public ImageAdapter(Context context, List<Long> imageIds) {
        this.imageIds = imageIds;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Long imageId = imageIds.get(position);
        loadImage(imageId, holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageIds.size();
    }

    private void loadImage(Long id, ImageView imageView) {
        Call<ResponseBody> getImage = ClientUtils.imageService.getById(id, false);
        getImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        File imageFile = createTempImageFile(bitmap);
                        Picasso.get().load(Uri.fromFile(imageFile)).into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else Log.e("OpenDoors", "Error getting image");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OpenDoors", "Error loading image");
            }
        });
    }

    private File createTempImageFile(Bitmap bitmap) throws IOException {
        File tempDir = context.getCacheDir();
        File tempFile = File.createTempFile("image", "png", tempDir);

        OutputStream os = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();

        return tempFile;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
