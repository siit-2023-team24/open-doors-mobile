package com.bookingapptim24.fragments.blocked_users;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.databinding.FragmentBlockedUserListBinding;
import com.bookingapptim24.databinding.FragmentUserReportListBinding;
import com.bookingapptim24.fragments.user_reports.UserReportAdapter;
import com.bookingapptim24.models.UserReport;
import com.bookingapptim24.models.UserSummary;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockedUserAdapter extends ArrayAdapter<UserSummary> {

    private ArrayList<UserSummary> users = new ArrayList<>();
    private Activity activity;
    private FragmentManager fragmentManager;

    public BlockedUserAdapter(@NonNull Activity context, FragmentManager fragmentManager, ArrayList<UserSummary> users) {
        super(context, R.layout.blocked_user_item, users);
        this.users = users;
        this.activity = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Nullable
    @Override
    public UserSummary getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserSummary user = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.blocked_user_item, parent, false);

        TextView username = convertView.findViewById(R.id.blocked_username);
        TextView firstName = convertView.findViewById(R.id.blocked_first_name);
        TextView lastName = convertView.findViewById(R.id.blocked_last_name);
        TextView role = convertView.findViewById(R.id.blocked_role);

        Button unblockBtn = convertView.findViewById(R.id.unblock_user_btn);
        unblockBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to unblock this user?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onUnblock(user);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });
        if (user != null) {
            username.setText(user.getUsername());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            role.setText(user.getRole());
        }
        return convertView;
    }

    private void onUnblock(UserSummary user) {
        Log.d("OpenDoors", "Unblock user: " + user.getId());
        Call<ResponseBody> call = ClientUtils.userService.unblock(user.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"User unblocked", Toast.LENGTH_SHORT).show();
                    users.remove(user);
                    BlockedUserAdapter.this.notifyDataSetChanged();
                } else {
                    Log.d("OpenDoors", "Message received: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
