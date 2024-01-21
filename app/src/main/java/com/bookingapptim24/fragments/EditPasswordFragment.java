package com.bookingapptim24.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bookingapptim24.R;
import com.bookingapptim24.clients.ClientUtils;
import com.bookingapptim24.models.NewPassword;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditPasswordFragment extends Fragment {

    private NewPassword dto = new NewPassword();

    private EditText currentPass;
    private EditText newPass;
    private EditText confirmPass;


    public EditPasswordFragment() {
        // Required empty public constructor
    }


    public static EditPasswordFragment newInstance() {
        return new EditPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dto.setUsername(getArguments().getString("username"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        currentPass = view.findViewById(R.id.currentPasswordET);
        newPass = view.findViewById(R.id.newPasswordET);
        confirmPass = view.findViewById(R.id.confirmPasswordET);

        Button save = view.findViewById(R.id.save_password_btn);
        save.setOnClickListener(v-> {
            dto.setOldPassword(currentPass.getText().toString());
            dto.setNewPassword(newPass.getText().toString());
            dto.setRepeatPassword(confirmPass.getText().toString());

            Call<ResponseBody> call = ClientUtils.userService.changePassword(dto);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Log.d("OpenDoors", "Message received");
                        Toast.makeText(requireActivity(), "Changed password", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                        navController.navigate(R.id.nav_profile);
                    } else {
                        Toast.makeText(requireActivity(), "Password changing failed", Toast.LENGTH_SHORT).show();
                        Log.d("OpenDoors", "Message received: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("OpenDoors", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });
        return view;
    }
}