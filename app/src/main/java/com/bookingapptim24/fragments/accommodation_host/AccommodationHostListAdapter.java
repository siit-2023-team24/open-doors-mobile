package com.bookingapptim24.fragments.accommodation_host;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.AccommodationHost;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationHostListAdapter extends ArrayAdapter<AccommodationHost> {

    private ArrayList<AccommodationHost> accommodations;

    private Activity activity;

    private FragmentManager fragmentManager;

    private SessionManager sessionManager;

    public AccommodationHostListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<AccommodationHost> accommodations) {
        super(context, R.layout.accommodation_host_item, accommodations);
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
            if (accommodation.getImage() != null && accommodation.getImage() > 0)
                loadImage(imageView, accommodation.getImage());
            else
                imageView.setImageResource(R.drawable.accommodation_image);
            accommodationName.setText(accommodation.getName() + " #" + accommodation.getId());
            accommodationCard.setOnClickListener(v -> {
                Log.i("OpenDoors", "Clicked: " + accommodation.getName() + ", id: " + accommodation.getId());
                Bundle args = new Bundle();
                args.putLong("accommodationId", accommodation.getId());
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

    private void loadImage(ImageView image, Long id) {
        Call<ResponseBody> getImage = ClientUtils.imageService.getById(id, false);
        getImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        File imageFile = createTempImageFile(bitmap);
                        Picasso.get().load(Uri.fromFile(imageFile)).into(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else Log.e("OpenDoors", "Error getting image");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OpenDoors", "Error loading image");
                image.setImageResource(R.drawable.accommodation_image);
            }
        });
    }

    private File createTempImageFile(Bitmap bitmap) throws IOException {
        File tempDir = getContext().getCacheDir();
        File tempFile = File.createTempFile("image", "png", tempDir);

        OutputStream os = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();

        return tempFile;
    }

}
