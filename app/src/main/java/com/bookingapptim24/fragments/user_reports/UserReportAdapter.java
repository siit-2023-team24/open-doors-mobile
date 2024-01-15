package com.bookingapptim24.fragments.user_reports;

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

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.UserReport;
import com.bookingapptim24.util.DataChangesListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportAdapter extends ArrayAdapter<UserReport> {

    private ArrayList<UserReport> reports;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private DataChangesListener listener;

    public UserReportAdapter(@NonNull Activity context, FragmentManager fragmentManager, ArrayList<UserReport> reports) {
        super(context, R.layout.user_report_item, reports);
        this.reports = reports;
        this.activity = context;
        this.fragmentManager = fragmentManager;
    }

    public void setListener(DataChangesListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Nullable
    @Override
    public UserReport getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserReport report = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_item, parent, false);

        TextView complaintentUsername = convertView.findViewById(R.id.report_item_complaintant_username);
        TextView timestamp = convertView.findViewById(R.id.report_item_timestamp);
        TextView status = convertView.findViewById(R.id.report_item_status);
        TextView reason = convertView.findViewById(R.id.report_item_reason);
        TextView recipientUsername = convertView.findViewById(R.id.report_item_recipient_username);

        Button dismissBtn = convertView.findViewById(R.id.dismiss_report_btn);
        dismissBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to dismiss this report?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onDismiss(report);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        Button blockBtn = convertView.findViewById(R.id.block_user_btn);
        blockBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Are you sure you want to block this user?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, id) -> {
                        onBlock(report);
                    })
                    .setNegativeButton("No", (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        if (report != null) {
            complaintentUsername.setText(report.getComplainantUsername());
            timestamp.setText(sdf.format(report.getTimestamp()));
            status.setText(report.getStatus().toLowerCase(Locale.ROOT).replace("role_", ""));
            reason.setText(report.getReason());
            recipientUsername.setText(report.getRecipientUsername());

            if (report.getStatus().equals("active")) {
                dismissBtn.setVisibility(View.VISIBLE);
                blockBtn.setVisibility(View.VISIBLE);
            } else {
                dismissBtn.setVisibility(View.GONE);
                blockBtn.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private void onBlock(UserReport report) {
        Log.d("OpenDoors", "Resolve report: " + report.getId());
        Call<ResponseBody> call = ClientUtils.userReportService.resolve(report.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received");
                    Toast.makeText(activity,"Report resolved", Toast.LENGTH_SHORT).show();
                    listener.onDataChanged();
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

    private void onDismiss(UserReport report) {
        Log.d("OpenDoors", "Dismiss report " + report.getId());
        Call<UserReport> call = ClientUtils.userReportService.dismiss(report.getId());
        call.enqueue(new Callback<UserReport>() {
            @Override
            public void onResponse(Call<UserReport> call, Response<UserReport> response) {
                if (response.code() == 200) {
                    Log.d("OpenDoors", "Message received: " + response.body());
                    Toast.makeText(activity,"Report dismissed", Toast.LENGTH_SHORT).show();
                    listener.onDataChanged();
                } else {
                    Log.d("OpenDoors", "Message received: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserReport> call, Throwable t) {
                Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
