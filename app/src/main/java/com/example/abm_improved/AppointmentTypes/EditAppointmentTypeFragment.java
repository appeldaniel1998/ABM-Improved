package com.example.abm_improved.AppointmentTypes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.AppointmentTypes.Templates.EnterAppointmentTypeDetails;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.AppointmentType;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;

public class EditAppointmentTypeFragment extends BaseFragment {

    private AppointmentType currAppointmentType;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_type_details, container, false);

        assert getArguments() != null;
        EditAppointmentTypeFragmentArgs args = EditAppointmentTypeFragmentArgs.fromBundle(getArguments());
        int appointmentTypeIndex = args.getAppointmentTypeId();
        currAppointmentType = DatabaseUtils.getAppointmentTypes().get(appointmentTypeIndex);

        navController = NavHostFragment.findNavController(EditAppointmentTypeFragment.this);

        EnterAppointmentTypeDetails enterAppointmentTypeDetails = new EnterAppointmentTypeDetails(view, currAppointmentType);

        enterAppointmentTypeDetails.getSaveButton().setOnClickListener(v -> {
            String typeName = enterAppointmentTypeDetails.getTypeName().getText().toString();
            String price = enterAppointmentTypeDetails.getPrice().getText().toString();
            String duration = enterAppointmentTypeDetails.getDuration().getText().toString();
            String uid = currAppointmentType.getUid();
            DatabaseUtils.addAppointmentTypeToDatabase(new AppointmentType(typeName, price, duration, uid));

            navController.popBackStack(); // Go back to previous fragment
        });

        enterAppointmentTypeDetails.getDeleteButton().setOnClickListener(v -> {
            // Ask for confirmation before deleting the client
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Deletion of Appointment Type");
            builder.setMessage("Are you sure you want to delete " + currAppointmentType.getTypeName() + "?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) { // Confirmation received
                    DatabaseUtils.deleteAppointmentTypeFromDatabase(currAppointmentType); // Delete Appointment type from database
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
