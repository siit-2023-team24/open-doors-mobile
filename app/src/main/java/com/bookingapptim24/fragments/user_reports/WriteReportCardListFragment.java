package com.bookingapptim24.fragments.user_reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.clients.SessionManager;
import com.bookingapptim24.databinding.FragmentWriteReportCardListBinding;
import com.bookingapptim24.util.DataChangesListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReportCardListFragment extends ListFragment implements DataChangesListener {

    private WriteReportCardAdapter adapter;
    private SessionManager sessionManager;
    private FragmentWriteReportCardListBinding binding;
    private List<String> usernames = new ArrayList<>();

    public WriteReportCardListFragment() {}

    public static WriteReportCardListFragment newInstance() {
        return new WriteReportCardListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWriteReportCardListBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireContext());
        adapter = new WriteReportCardAdapter(getActivity(), getActivity().getSupportFragmentManager(), usernames);
        adapter.setListener(WriteReportCardListFragment.this);
        setListAdapter(adapter);

        String reportableUsersText = requireContext().getString(R.string.guests_you_have_hosted);
        if (sessionManager.getRole().equals("ROLE_GUEST")) {
            reportableUsersText = requireContext().getString(R.string.hosts_you_have_stayed_with);
        }
        binding.reportableUsersText.setText(reportableUsersText);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getListView().setDividerHeight(2);
        getData();
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

    private void getData() {
        Call<List<String>> call = ClientUtils.userReportService.getReportableUsersForUser(sessionManager.getUserId(),
                String.valueOf(sessionManager.getRole().equals("ROLE_GUEST")));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()){
                    binding.noUsersText.setVisibility(View.GONE);
                    usernames.clear();
                    usernames.addAll(response.body());
                    if(usernames.isEmpty()) {
                        String usersType = "guests";
                        if (sessionManager.getRole().equals("ROLE_GUEST")) usersType="hosts";
                        binding.noUsersText.setText(requireContext().getString(R.string.no_users_to_report, usersType));
                        binding.noUsersText.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onDataChanged() {
        getData();
    }
}
