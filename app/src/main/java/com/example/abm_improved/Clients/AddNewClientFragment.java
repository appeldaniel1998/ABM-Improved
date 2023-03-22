package com.example.abm_improved.Clients;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Templates.EnterClientDetails;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DatePicker;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class AddNewClientFragment extends BaseFragment {

    private EnterClientDetails enterClientDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_client_details, container, false);

        enterClientDetails = new EnterClientDetails(view, onChooseProfilePicListener, requireActivity(), EnterClientDetails.ADD_CLIENT);
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
                DatabaseUtils.uploadRelevantClientInfo(userToAdd,
                        requireActivity(),
                        FirebaseStorage.getInstance().getReference().child("Clients").child(currClientUid).child("profile.jpg"));

                // if successful, go back to the previous fragment
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClientsMainFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }
}