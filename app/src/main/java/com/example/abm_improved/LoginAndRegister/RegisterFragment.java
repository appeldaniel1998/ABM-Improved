package com.example.abm_improved.LoginAndRegister;

import static com.example.abm_improved.BaseActivity.profilePicSelected;
import static com.example.abm_improved.BaseActivity.profilePicUri;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Templates.EnterClientDetails;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DatePicker;
import com.example.abm_improved.Utils.Interfaces.OnChooseProfilePicListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends BaseFragment {

    private FirebaseAuth auth;

    private EnterClientDetails enterClientDetails;

    private OnChooseProfilePicListener onChooseProfilePicListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clients_templates_enter_client_details, container, false);
        enterClientDetails = new EnterClientDetails(view, onChooseProfilePicListener, requireActivity(), EnterClientDetails.USER);

        requireActivity().setTitle("Register New User");
        auth = super.getAuth();

        enterClientDetails.getRegisterButton().setOnClickListener(v -> {
            String firstNameStr = enterClientDetails.getFirstNameEditText().getText().toString();
            String lastNameStr = enterClientDetails.getLastNameEditText().getText().toString();
            String emailStr = enterClientDetails.getEmailEditText().getText().toString();
            String passwordStr = enterClientDetails.getPasswordEditText().getText().toString();
            String confirmPasswordStr = enterClientDetails.getConfirmPasswordEditText().getText().toString();
            String phoneNumberStr = enterClientDetails.getPhoneNumberEditText().getText().toString();
            String addressStr = enterClientDetails.getAddressEditText().getText().toString();
            String birthdayStr = enterClientDetails.getBirthdayTextView().getText().toString();
            if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty() || birthdayStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!passwordStr.equals(confirmPasswordStr)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, passwordStr, DatePicker.stringToInt(birthdayStr), auth);
            }
        });

        return view;
    }

    public void registerUser(String textFirstName, String textLastName, String textEmail, String textPhoneNumber, String textAddress, String textPassword, int textBirthdayDate, FirebaseAuth auth) {
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(task -> {//ask firebase auth to create a new user
            if (task.isSuccessful()) { // if authenticator succeeded in creating a user
                FirebaseUser user = auth.getCurrentUser();//take that user

                assert user != null;
                String userUID = user.getUid();//get user ID

                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, userUID, false); //creating a new user
                DatabaseUtils.addClientToFirebase(userToAdd, requireActivity());//add the user to the database

                if (profilePicSelected) {
                    DatabaseUtils.uploadImageToFirebase(super.getStorageReference(), userUID, profilePicUri, requireActivity());
                }

                // Upon success and finishing:

                // 1. Init menu sidebar again (to show the correct menu items and user)
                BaseActivity baseActivity = (BaseActivity) requireActivity();
                baseActivity.initMenuSideBar();

                // 2. Move to appointments main activity
                Bundle bundle = new Bundle();
                AppointmentsMainFragment appointmentsMainFragment = new AppointmentsMainFragment();
                appointmentsMainFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsMainFragment).addToBackStack(null).commit();
            } else {
                Toast.makeText(requireActivity(), "Registration failed! Unable to authenticate new user", Toast.LENGTH_SHORT).show();
            }
        });
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
}