package com.example.abm_improved.Clients;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Templates.EnterClientDetails;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DatePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class EditClientFragment extends BaseFragment {

    EnterClientDetails enterClientDetails;
    StorageReference profilePicReference;


    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.templates_enter_client_details, container, false);

            // Getting index of current client from previous fragment
            Bundle args = getArguments();
            assert args != null;
            int clientIndex = Integer.parseInt(args.getString("clientIndex"));
            Client currClient = DatabaseUtils.getClients().get(clientIndex);

            enterClientDetails = new EnterClientDetails(view, onChooseProfilePicListener, requireActivity(), EnterClientDetails.EDIT_CLIENT);
            profilePicReference = FirebaseStorage.getInstance().getReference().child("Clients").child(currClient.getUid()).child("profile.jpg");

            requireActivity().setTitle("Edit Client");
            enterClientDetails.setValuesToXmlFields(currClient);

            enterClientDetails.getRegisterButton().setOnClickListener(v -> {
                String firstNameStr = enterClientDetails.getFirstNameEditText().getText().toString();
                String lastNameStr = enterClientDetails.getLastNameEditText().getText().toString();
                String emailStr = enterClientDetails.getEmailEditText().getText().toString();
                String phoneNumberStr = enterClientDetails.getPhoneNumberEditText().getText().toString();
                String addressStr = enterClientDetails.getAddressEditText().getText().toString();
                String birthdayStr = enterClientDetails.getBirthdayTextView().getText().toString();
                if (firstNameStr.isEmpty() || lastNameStr.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    Client updatedClient = new Client(firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, DatePicker.stringToInt(birthdayStr), currClient.getUid(), false); // creating a new client
                    DatabaseUtils.uploadRelevantClientInfo(updatedClient,
                            requireActivity(),
                            profilePicReference);

                    // if successful, go back to the previous fragment
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClientsMainFragment()).commit();
                }
            });

            //Connecting with Firebase storage and retrieving image
            DatabaseUtils.loadImageToImageView(profilePicReference, enterClientDetails.getProfilePicImageView(), requireActivity());

            enterClientDetails.getDeleteButton().setOnClickListener(v -> {
                //Ask for confirmation before deleting the client
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Deletion of Client");
                builder.setMessage("Are you sure you want to delete " + currClient.getFullName() + " and all his/her data?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) { // Confirmation received
                        DatabaseUtils.deleteClient(currClient.getUid()); // Delete client from database
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClientsMainFragment()).commit(); // Go back to previous fragment
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();

            });

            return view;
        }
}