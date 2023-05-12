package com.example.abm_improved.Clients;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.Clients.Templates.EnterClientDetails;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DateUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class EditClientFragment extends BaseFragment {

    private EnterClientDetails enterClientDetails;
    private StorageReference profilePicReference;

    private Client currClient;

    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.templates_enter_client_details, container, false);

        navController = NavHostFragment.findNavController(EditClientFragment.this);

        // Getting index of current client from previous fragment
        assert getArguments() != null;
        EditClientFragmentArgs args = EditClientFragmentArgs.fromBundle(getArguments());
        int clientIndex = args.getClientIndex();
        currClient = DatabaseUtils.getClients().get(clientIndex);

        enterClientDetails = new EnterClientDetails(view, onChooseProfilePicListener, requireActivity(), EnterClientDetails.EDIT_CLIENT);
        profilePicReference = FirebaseStorage.getInstance().getReference().child("Clients").child(currClient.getUid()).child("profile.jpg");

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
                Client updatedClient = new Client(firstNameStr, lastNameStr, emailStr, phoneNumberStr, addressStr, DateUtils.stringToInt(birthdayStr), currClient.getUid(), currClient.getManager()); // creating a new client
                DatabaseUtils.uploadRelevantClientInfo(updatedClient,
                        requireActivity(),
                        profilePicReference);

                navController.popBackStack(); // if successful, go back to the previous fragment
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
                    navController.popBackStack(); // Go back to previous fragment
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        return view;
    }
}