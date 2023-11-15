// Generated by view binder compiler. Do not edit!
package com.bookingapptim24.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.bookingapptim24.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterScreenBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button backButton;

  @NonNull
  public final Spinner countrySpinner;

  @NonNull
  public final RadioButton radioOption1;

  @NonNull
  public final RadioButton radioOption2;

  @NonNull
  public final LinearLayout registerAddressLayout;

  @NonNull
  public final LinearLayout registerButtonsLayout;

  @NonNull
  public final LinearLayout registerCityLayout;

  @NonNull
  public final LinearLayout registerConfirmPasswordLayout;

  @NonNull
  public final LinearLayout registerCountryLayout;

  @NonNull
  public final LinearLayout registerEmailLayout;

  @NonNull
  public final LinearLayout registerFirstNameLayout;

  @NonNull
  public final LinearLayout registerLastNameLayout;

  @NonNull
  public final LinearLayout registerPasswordLayout;

  @NonNull
  public final LinearLayout registerPhoneLayout;

  @NonNull
  public final LinearLayout registerRoleLayout;

  @NonNull
  public final Button signUpButton;

  private ActivityRegisterScreenBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button backButton, @NonNull Spinner countrySpinner,
      @NonNull RadioButton radioOption1, @NonNull RadioButton radioOption2,
      @NonNull LinearLayout registerAddressLayout, @NonNull LinearLayout registerButtonsLayout,
      @NonNull LinearLayout registerCityLayout, @NonNull LinearLayout registerConfirmPasswordLayout,
      @NonNull LinearLayout registerCountryLayout, @NonNull LinearLayout registerEmailLayout,
      @NonNull LinearLayout registerFirstNameLayout, @NonNull LinearLayout registerLastNameLayout,
      @NonNull LinearLayout registerPasswordLayout, @NonNull LinearLayout registerPhoneLayout,
      @NonNull LinearLayout registerRoleLayout, @NonNull Button signUpButton) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.countrySpinner = countrySpinner;
    this.radioOption1 = radioOption1;
    this.radioOption2 = radioOption2;
    this.registerAddressLayout = registerAddressLayout;
    this.registerButtonsLayout = registerButtonsLayout;
    this.registerCityLayout = registerCityLayout;
    this.registerConfirmPasswordLayout = registerConfirmPasswordLayout;
    this.registerCountryLayout = registerCountryLayout;
    this.registerEmailLayout = registerEmailLayout;
    this.registerFirstNameLayout = registerFirstNameLayout;
    this.registerLastNameLayout = registerLastNameLayout;
    this.registerPasswordLayout = registerPasswordLayout;
    this.registerPhoneLayout = registerPhoneLayout;
    this.registerRoleLayout = registerRoleLayout;
    this.signUpButton = signUpButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterScreenBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterScreenBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register_screen, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterScreenBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back_button;
      Button backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.country_spinner;
      Spinner countrySpinner = ViewBindings.findChildViewById(rootView, id);
      if (countrySpinner == null) {
        break missingId;
      }

      id = R.id.radioOption1;
      RadioButton radioOption1 = ViewBindings.findChildViewById(rootView, id);
      if (radioOption1 == null) {
        break missingId;
      }

      id = R.id.radioOption2;
      RadioButton radioOption2 = ViewBindings.findChildViewById(rootView, id);
      if (radioOption2 == null) {
        break missingId;
      }

      id = R.id.register_address_layout;
      LinearLayout registerAddressLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerAddressLayout == null) {
        break missingId;
      }

      id = R.id.register_buttons_layout;
      LinearLayout registerButtonsLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerButtonsLayout == null) {
        break missingId;
      }

      id = R.id.register_city_layout;
      LinearLayout registerCityLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerCityLayout == null) {
        break missingId;
      }

      id = R.id.register_confirm_password_layout;
      LinearLayout registerConfirmPasswordLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerConfirmPasswordLayout == null) {
        break missingId;
      }

      id = R.id.register_country_layout;
      LinearLayout registerCountryLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerCountryLayout == null) {
        break missingId;
      }

      id = R.id.register_email_layout;
      LinearLayout registerEmailLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerEmailLayout == null) {
        break missingId;
      }

      id = R.id.register_first_name_layout;
      LinearLayout registerFirstNameLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerFirstNameLayout == null) {
        break missingId;
      }

      id = R.id.register_last_name_layout;
      LinearLayout registerLastNameLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerLastNameLayout == null) {
        break missingId;
      }

      id = R.id.register_password_layout;
      LinearLayout registerPasswordLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerPasswordLayout == null) {
        break missingId;
      }

      id = R.id.register_phone_layout;
      LinearLayout registerPhoneLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerPhoneLayout == null) {
        break missingId;
      }

      id = R.id.register_role_layout;
      LinearLayout registerRoleLayout = ViewBindings.findChildViewById(rootView, id);
      if (registerRoleLayout == null) {
        break missingId;
      }

      id = R.id.sign_up_button;
      Button signUpButton = ViewBindings.findChildViewById(rootView, id);
      if (signUpButton == null) {
        break missingId;
      }

      return new ActivityRegisterScreenBinding((ConstraintLayout) rootView, backButton,
          countrySpinner, radioOption1, radioOption2, registerAddressLayout, registerButtonsLayout,
          registerCityLayout, registerConfirmPasswordLayout, registerCountryLayout,
          registerEmailLayout, registerFirstNameLayout, registerLastNameLayout,
          registerPasswordLayout, registerPhoneLayout, registerRoleLayout, signUpButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
