package com.example.abm_improved.Clients;

import static com.example.abm_improved.BaseActivity.profilePicSelected;
import static com.example.abm_improved.BaseActivity.profilePicUri;
import static com.example.abm_improved.Utils.DatabaseUtils.registerNewUser;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.abm_improved.Appointments.AppointmentsMainFragment;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Templates.EnterClientDetails;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DatePicker;
import com.example.abm_improved.Utils.Interfaces;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class AddNewClientFragment extends BaseFragment {

    private EnterClientDetails enterClientDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clients_templates_enter_client_details, container, false);

        enterClientDetails = new EnterClientDetails(view, onChooseProfilePicListener, requireActivity(), EnterClientDetails.CLIENT);
        requireActivity().setTitle("Add New Client");

        enterClientDetails.getRegisterButton().setOnClickListener(v -> {
            String firstNameStr = enterClientDetails.getFirstNameEditText().getText().toString();
            String lastNameStr = enterClientDetails.getLastNameEditText().getText().toString();
            String emailStr = enterClientDetails.getEmailEditText().getText().toString();
            String phoneNumberStr = enterClientDetails.getPhoneNumberEditText().getText().toString();
            String addressStr = enterClientDetails.getAddressEditText().getText().toString();
            String birthdayStr = enterClientDetails.getBirthdayTextView().getText().toString();
            if (firstNameStr.isEmpty() || lastNameStr.isEmpty()) { //required fields are first name and last name only //TODO: LATER - verify required fields
                Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else { // Create new client, add to database and upload profile pic
                String currClientUid = UUID.randomUUID().toString(); // Creating UID for the new user

                Client userToAdd = new Client(firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, DatePicker.stringToInt(birthdayStr), currClientUid, false); //creating a new client
                DatabaseUtils.addClientToFirebase(userToAdd, requireActivity()); //Add the client to the database

                if (profilePicSelected) {
                    DatabaseUtils.uploadImageToFirebase(FirebaseStorage.getInstance().getReference().child("Clients").child(currClientUid).child("profile.jpg"), //path to save the pic
                            profilePicUri, // pic to save
                            requireActivity()); // current activity
                }

                // if successful, go back to the previous fragment
                Bundle bundle = new Bundle();
                ClientsMainFragment clientsMainFragment = new ClientsMainFragment();
                clientsMainFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, clientsMainFragment).commit();
            }
        });

        return view;
    }
}