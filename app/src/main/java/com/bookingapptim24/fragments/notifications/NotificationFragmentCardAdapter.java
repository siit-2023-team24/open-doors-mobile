package com.bookingapptim24.fragments.notifications;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bookingapptim24.R;
import com.bookingapptim24.models.NotificationDTO;
import com.bookingapptim24.util.DataChangesListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationFragmentCardAdapter extends ArrayAdapter<NotificationDTO> {

    private List<NotificationDTO> notifications;
    private Activity activity;
    private FragmentManager fragmentManager;
    private DataChangesListener listener;
    public NotificationFragmentCardAdapter(@NonNull Activity context, FragmentManager fragmentManager, List<NotificationDTO> notifications) {
        super(context, R.layout.notification_card_item, notifications);
        this.notifications = notifications;
        this.activity = context;
        this.fragmentManager = fragmentManager;
    }

    public void setListener(DataChangesListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Nullable
    @Override
    public NotificationDTO getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String viewDate(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(activity.getString(R.string.date_format));
        return sdf.format(new Timestamp(date));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotificationDTO notificationDTO = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card_item, parent, false);

        TextView message = convertView.findViewById(R.id.notificationMessageTextView);
        TextView type = convertView.findViewById(R.id.notificationTypeTextView);
        TextView timestamp = convertView.findViewById(R.id.notificationTimestampTextView);
        if (notificationDTO != null) {
            message.setText(notificationDTO.getMessage());
            type.setText(notificationDTO.getType().getTypeMessage());
            timestamp.setText(viewDate(notificationDTO.getTimestamp()));
        }
        return convertView;
    }
}
