package com.example.abm_improved.LoginAndRegister;

import static com.example.abm_improved.Utils.DatabaseUtils.registerNewUser;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Templates.EnterClientDetails;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatePicker;
import com.example.abm_improved.Utils.Interfaces.OnChooseProfilePicListener;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends BaseFragment {

    private EnterClientDetails enterClientDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clients_templates_enter_client_details, container, false);

        enterClientDetails = new EnterClientDetails(view, onChooseProfilePicListener, requireActivity(), EnterClientDetails.USER);

        requireActivity().setTitle("Register New User");

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
                registerNewUser(requireActivity(), firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, passwordStr, DatePicker.stringToInt(birthdayStr));

                // Move to appointments main fragment
                Bundle bundle = new Bundle();
                AppointmentsMainFragment appointmentsMainFragment = new AppointmentsMainFragment();
                appointmentsMainFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, appointmentsMainFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
}