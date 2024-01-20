package com.bookingapptim24.fragments.user_reports;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.models.NewUserReport;
import com.bookingapptim24.models.UserReport;
import com.bookingapptim24.util.DataChangesListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReportCardAdapter extends ArrayAdapter<String> {

    private List<String> usernames;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SessionManager sessionManager;
    private DataChangesListener listener;

    public WriteReportCardAdapter(@NonNull Activity context, FragmentManager fragmentManager, List<String> usernames) {
        super(context, R.layout.write_report_card_item, usernames);
        this.sessionManager = new SessionManager(context);
        this.usernames = usernames;
        this.activity = context;
        this.fragmentManager = fragmentManager;
    }

    public void setListener(DataChangesListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return usernames.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return usernames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String username = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.write_report_card_item, parent, false);

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        if(username != null) {
            usernameTextView.setText(username);
        }
        TextInputEditText reasonEditText = convertView.findViewById(R.id.reasonEditText);
        TextView errorTextView = convertView.findViewById(R.id.errorTextView);
        Button reportButton = convertView.findViewById(R.id.reportButton);

        reportButton.setOnClickListener(v -> {
            String reason = reasonEditText.getText().toString();
            if (reason == null || reason.equals("")) {
                errorTextView.setVisibility(View.VISIBLE);
                return;
            }
            errorTextView.setVisibility(View.GONE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage(activity.getString(R.string.dialog_report_user))
                    .setCancelable(false)
                    .setPositiveButton(activity.getString(R.string.yes), (dialogInterface, id) -> {
                        NewUserReport dto = new NewUserReport(
                                username,
                                sessionManager.getUsername(),
                                sessionManager.getRole().equals("ROLE_GUEST"),
                                reason
                        );
                        Call<UserReport> call = ClientUtils.userReportService.createUserReview(dto);
                        call.enqueue(new Callback<UserReport>() {
                            @Override
                            public void onResponse(Call<UserReport> call, Response<UserReport> response) {
                                if (response.isSuccessful()){
                                    Log.d("Response", response.message());
                                    listener.onDataChanged();
                                } else {
                                    Log.d("REZ","Message received: "+response.code());
                                }
                            }
                            @Override
                            public void onFailure(Call<UserReport> call, Throwable t) {
                                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                            }
                        });
                    })
                    .setNegativeButton(activity.getString(R.string.no), (dialogInterface, id) -> dialogInterface.cancel());
            dialog.create().show();
        });

        return convertView;
    }

}
