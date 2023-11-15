// Generated by view binder compiler. Do not edit!
package com.bookingapptim24.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.bookingapptim24.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginScreenBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText editTextEmail;

  @NonNull
  public final EditText editTextPassword;

  @NonNull
  public final Button loginButton;

  @NonNull
  public final Button registerButton;

  private ActivityLoginScreenBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText editTextEmail, @NonNull EditText editTextPassword,
      @NonNull Button loginButton, @NonNull Button registerButton) {
    this.rootView = rootView;
    this.editTextEmail = editTextEmail;
    this.editTextPassword = editTextPassword;
    this.loginButton = loginButton;
    this.registerButton = registerButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginScreenBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginScreenBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login_screen, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginScreenBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.editTextEmail;
      EditText editTextEmail = ViewBindings.findChildViewById(rootView, id);
      if (editTextEmail == null) {
        break missingId;
      }

      id = R.id.editTextPassword;
      EditText editTextPassword = ViewBindings.findChildViewById(rootView, id);
      if (editTextPassword == null) {
        break missingId;
      }

      id = R.id.login_button;
      Button loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.register_button;
      Button registerButton = ViewBindings.findChildViewById(rootView, id);
      if (registerButton == null) {
        break missingId;
      }

      return new ActivityLoginScreenBinding((ConstraintLayout) rootView, editTextEmail,
          editTextPassword, loginButton, registerButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
