package com.bookingapptim24.fragments.notifications;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentNotificationsBinding;
import com.bookingapptim24.models.NotificationDTO;
import com.bookingapptim24.models.enums.NotificationType;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends ListFragment implements DataChangesListener {

    private NotificationFragmentCardAdapter adapter;
    private SessionManager sessionManager;
    private FragmentNotificationsBinding binding;
    private List<NotificationDTO> notifications = new ArrayList<>();
    private List<Boolean> ability = new ArrayList<>();

    private final List<NotificationType> allTypes = new ArrayList<NotificationType>() {{
        add(NotificationType.NEW_RESERVATION_REQUEST);
        add(NotificationType.RESERVATION_REQUEST);
        add(NotificationType.HOST_REVIEW);
        add(NotificationType.ACCOMMODATION_REVIEW);
    }};

    public NotificationsFragment() {}

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireContext());
        adapter = new NotificationFragmentCardAdapter(getActivity(), getActivity().getSupportFragmentManager(), notifications);
        adapter.setListener(NotificationsFragment.this);
        setListAdapter(adapter);
        if(sessionManager.isLoggedIn() && !sessionManager.getRole().equals("ROLE_HOST")) {
            binding.reservationRequestsToggle.setVisibility(View.GONE);
            binding.accommodationReviewsToggle.setVisibility(View.GONE);
            binding.hostReviewsToggle.setVisibility(View.GONE);
        }
        for(int i=0;i<4;i++) ability.add(true);
        getDisabled();
        binding.reservationRequestsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ability.set(0, true);
                } else {
                    ability.set(0, false);
                }
            }
        });

        binding.statusChangesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ability.set(1, true);
                } else {
                    ability.set(1, false);
                }
            }
        });

        binding.hostReviewsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ability.set(2, true);
                } else {
                    ability.set(2, false);
                }
            }
        });

        binding.accommodationReviewsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ability.set(3, true);
                } else {
                    ability.set(3, false);
                }
            }
        });

        binding.saveButton.setOnClickListener(v -> {
            saveSettings();
        });

        return binding.getRoot();
    }

    private void initializeSwitch(int i) {
        if (i==0) binding.reservationRequestsToggle.setChecked(false);
        else if (i==1) binding.statusChangesToggle.setChecked(false);
        else if (i==2) binding.hostReviewsToggle.setChecked(false);
        else if (i==3) binding.accommodationReviewsToggle.setChecked(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getListView().setDividerHeight(2);
        getNotifications();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getDisabled() {
        Call<List<NotificationType>> call = ClientUtils.notificationService.getDisabledFor(sessionManager.getUserId());
        call.enqueue(new Callback<List<NotificationType>>() {
            @Override
            public void onResponse(Call<List<NotificationType>> call, Response<List<NotificationType>> response) {
                if (response.isSuccessful()){
                    for(NotificationType notificationType : response.body()) {
                        initializeSwitch(notificationType.ordinal());
                        ability.set(notificationType.ordinal(), false);
                    }
                    Log.d("ability", ability.toString());
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NotificationType>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void getNotifications() {
        Call<List<NotificationDTO>> call = ClientUtils.notificationService.getAllFor(sessionManager.getUserId());
        call.enqueue(new Callback<List<NotificationDTO>>() {
            @Override
            public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                if (response.isSuccessful()){
                    binding.noDataTextView.setVisibility(View.GONE);
                    List<NotificationDTO> dtos = response.body();
                    if(dtos.isEmpty()) {
                        binding.noDataTextView.setVisibility(View.VISIBLE);
                    }
                    notifications.clear();
                    notifications.addAll(dtos);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void saveSettings() {
        List<NotificationType> disabled = new ArrayList<>();
        if(!ability.get(0)) disabled.add(NotificationType.NEW_RESERVATION_REQUEST);
        if(!ability.get(1)) disabled.add(NotificationType.RESERVATION_REQUEST);
        if(!ability.get(2)) disabled.add(NotificationType.HOST_REVIEW);
        if(!ability.get(3)) disabled.add(NotificationType.ACCOMMODATION_REVIEW);
        Call<ResponseBody> call = ClientUtils.notificationService.setDisabledFor(sessionManager.getUserId(), disabled);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onDataChanged() {

    }
}