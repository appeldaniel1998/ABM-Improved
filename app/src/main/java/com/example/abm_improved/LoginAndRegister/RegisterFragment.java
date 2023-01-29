package com.example.abm_improved.LoginAndRegister;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Client;
import com.example.abm_improved.DatePicker;
import com.example.abm_improved.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends BaseFragment {

    private FirebaseAuth auth;

    // basic XML fields
    private ImageView profilePicImageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private LinearLayout birthdayLinearLayout;
    private TextView birthdayTextView;
    private Button registerButton;

    //for date picker
    private DatePickerDialog datePickerDialog;

    //for choosing profile pic
    private OnChooseProfilePicListener onChooseProfilePicListener;
    public static boolean profilePicSelected = false;
    public static Uri profilePicUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initFields(view);

        requireActivity().setTitle("Register New User");
        auth = super.getAuth();
        datePickerDialog = DatePicker.initDatePicker(birthdayTextView, requireActivity());
        birthdayTextView.setText(DatePicker.getTodayDate()); // Set initial date to today's date

        birthdayLinearLayout.setOnClickListener(v -> datePickerDialog.show()); //onclick listener for birthdayLinearLayout to choose a date

        registerButton.setOnClickListener(v -> {
            String firstNameStr = firstNameEditText.getText().toString();
            String lastNameStr = lastNameEditText.getText().toString();
            String emailStr = emailEditText.getText().toString();
            String passwordStr = passwordEditText.getText().toString();
            String confirmPasswordStr = confirmPasswordEditText.getText().toString();
            String phoneNumberStr = phoneNumberEditText.getText().toString();
            String addressStr = addressEditText.getText().toString();
            String birthdayStr = birthdayTextView.getText().toString();
            if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty() || birthdayStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!passwordStr.equals(confirmPasswordStr)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, passwordStr, DatePicker.stringToInt(birthdayStr), auth);
            }
        });

        profilePicImageView.setOnClickListener(v -> {
            onChooseProfilePicListener.onImageClick(profilePicImageView);
        });

        return view;
    }

    public void registerUser(String textFirstName, String textLastName, String textEmail, String textPhoneNumber,
                             String textAddress, String textPassword, int textBirthdayDate, FirebaseAuth auth) {
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(task -> {//ask firebase auth to create a new user
            if (task.isSuccessful()) { // if authenticator succeeded in creating a user
                FirebaseUser user = auth.getCurrentUser();//take that user

                assert user != null;
                String userUID = user.getUid();//get user ID

                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, userUID); //creating a new user
                LoginDatabaseUtils.addClientToFirebase(userToAdd, userUID, requireActivity());//add the user to the database

                if (profilePicSelected) {
                    LoginDatabaseUtils.uploadImageToFirebase(super.getStorageReference(), userUID, profilePicUri, requireActivity());
                }

                //upon success, move to appointments main activity
                Bundle bundle = new Bundle();
                AppointmentsMainFragment appointmentsMainFragment = new AppointmentsMainFragment();
                appointmentsMainFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsMainFragment).addToBackStack(null).commit();
            } else {
                Toast.makeText(requireActivity(), "Registration failed! Unable to authenticate new user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFields(View view) {
        profilePicImageView = view.findViewById(R.id.personIcon);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.retypePasswordEditText);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        birthdayLinearLayout = view.findViewById(R.id.birthdayLinearLayout);
        birthdayTextView = view.findViewById(R.id.birthdayDatePicker);
        registerButton = view.findViewById(R.id.registerButton);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnChooseProfilePicListener) {
            onChooseProfilePicListener = (OnChooseProfilePicListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnChooseMediaListener");
        }
    }

    public interface OnChooseProfilePicListener {
        void onImageClick(ImageView imageView);
    }
}