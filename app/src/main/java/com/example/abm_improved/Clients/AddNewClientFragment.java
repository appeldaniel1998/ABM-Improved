package com.example.abm_improved.Clients;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm_improved.LoginAndRegister.RegisterFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatePicker;
import com.google.firebase.auth.FirebaseAuth;

public class AddNewClientFragment extends Fragment {

    private FirebaseAuth auth;

    // basic XML fields
    private ImageView profilePicImageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private LinearLayout birthdayLinearLayout;
    private TextView birthdayTextView;
    private Button registerButton;

    //for date picker
    private DatePickerDialog datePickerDialog;

    //for choosing profile pic
    private RegisterFragment.OnChooseProfilePicListener onChooseProfilePicListener;
    public static boolean profilePicSelected = false;
    public static Uri profilePicUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template_input_info_client_user, container, false);
        initFields(view);

        requireActivity().setTitle("Add New Client");
        datePickerDialog = DatePicker.initDatePicker(birthdayTextView, requireActivity());
        birthdayTextView.setText(DatePicker.getTodayDate()); // Set initial date to today's date
        birthdayLinearLayout.setOnClickListener(v -> datePickerDialog.show()); //onclick listener for birthdayLinearLayout to choose a date

        registerButton.setOnClickListener(v -> {
            String firstNameStr = firstNameEditText.getText().toString();
            String lastNameStr = lastNameEditText.getText().toString();
            String emailStr = emailEditText.getText().toString();
            String phoneNumberStr = phoneNumberEditText.getText().toString();
            String addressStr = addressEditText.getText().toString();
            String birthdayStr = birthdayTextView.getText().toString();
            addNewUser(firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, DatePicker.stringToInt(birthdayStr));
        });


        return view;
    }

    private void initFields(View view) {
        profilePicImageView = view.findViewById(R.id.personIcon);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        birthdayLinearLayout = view.findViewById(R.id.birthdayLinearLayout);
        birthdayTextView = view.findViewById(R.id.birthdayDatePicker);
        registerButton = view.findViewById(R.id.registerButton);

        view.findViewById(R.id.passwordEditText).setVisibility(View.GONE);
        view.findViewById(R.id.retypePasswordEditText).setVisibility(View.GONE);
        view.findViewById(R.id.titleLinearLayout).setVisibility(View.GONE);
    }
}