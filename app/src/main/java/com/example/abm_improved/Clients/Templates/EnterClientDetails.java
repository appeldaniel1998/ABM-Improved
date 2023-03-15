package com.example.abm_improved.Clients.Templates;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatePicker;
import com.example.abm_improved.Utils.Interfaces;

public class EnterClientDetails {

    // basic XML fields
    private final ImageView profilePicImageView;
    private final EditText firstNameEditText;
    private final EditText lastNameEditText;
    private final EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private final EditText phoneNumberEditText;
    private final EditText addressEditText;
    private final LinearLayout birthdayLinearLayout;
    private final TextView birthdayTextView;
    private final Button registerButton;
    // --------------------

    public static final int USER = 0;
    public static final int CLIENT = 1; // A non-user client

    private final DatePickerDialog datePickerDialog; //for date picker
    private final Interfaces.OnChooseProfilePicListener onChooseProfilePicListener; //for choosing profile pic


    /**
     * Constructor for new USER details (including password and confirm password)
     *
     * @param view
     * @param onChooseProfilePicListener
     * @param activity
     */
    public EnterClientDetails(View view,
                              Interfaces.OnChooseProfilePicListener onChooseProfilePicListener,
                              FragmentActivity activity,
                              int userType) {

        this.profilePicImageView = view.findViewById(R.id.personIcon);
        this.firstNameEditText = view.findViewById(R.id.firstNameEditText);
        this.lastNameEditText = view.findViewById(R.id.lastNameEditText);
        this.emailEditText = view.findViewById(R.id.emailEditText);
        this.phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        this.addressEditText = view.findViewById(R.id.addressEditText);
        this.birthdayLinearLayout = view.findViewById(R.id.birthdayLinearLayout);
        this.birthdayTextView = view.findViewById(R.id.birthdayDatePicker);
        this.registerButton = view.findViewById(R.id.registerButton);
        this.passwordEditText = view.findViewById(R.id.passwordEditText);
        this.confirmPasswordEditText = view.findViewById(R.id.retypePasswordEditText);
        this.onChooseProfilePicListener = onChooseProfilePicListener;

        this.datePickerDialog = DatePicker.initDatePicker(birthdayTextView, activity);
        this.birthdayTextView.setText(DatePicker.getTodayDate()); // Set initial date to today's date
        this.birthdayLinearLayout.setOnClickListener(v -> datePickerDialog.show()); //onclick listener for birthdayLinearLayout to choose a date

        this.profilePicImageView.setOnClickListener(v -> {
            this.onChooseProfilePicListener.onImageClick(profilePicImageView);
        });


        if (userType == CLIENT) { //if a user is passed, then it is required to include password and confirm password fields
            this.passwordEditText.setVisibility(View.GONE);
            this.confirmPasswordEditText.setVisibility(View.GONE);
            view.findViewById(R.id.title).setVisibility(View.GONE);
        }
    }


    public ImageView getProfilePicImageView() {
        return profilePicImageView;
    }

    public EditText getFirstNameEditText() {
        return firstNameEditText;
    }

    public EditText getLastNameEditText() {
        return lastNameEditText;
    }

    public EditText getEmailEditText() {
        return emailEditText;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public EditText getConfirmPasswordEditText() {
        return confirmPasswordEditText;
    }

    public EditText getPhoneNumberEditText() {
        return phoneNumberEditText;
    }

    public EditText getAddressEditText() {
        return addressEditText;
    }

    public LinearLayout getBirthdayLinearLayout() {
        return birthdayLinearLayout;
    }

    public TextView getBirthdayTextView() {
        return birthdayTextView;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public DatePickerDialog getDatePickerDialog() {
        return datePickerDialog;
    }

    public Interfaces.OnChooseProfilePicListener getOnChooseProfilePicListener() {
        return onChooseProfilePicListener;
    }
}
