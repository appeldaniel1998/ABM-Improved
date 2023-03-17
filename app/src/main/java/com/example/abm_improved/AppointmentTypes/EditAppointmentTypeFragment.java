package com.example.abm_improved.AppointmentTypes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.AppointmentTypes.Templates.EnterAppointmentTypeDetails;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.AppointmentType;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;

public class EditAppointmentTypeFragment extends BaseFragment {

    AppointmentType currAppointmentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_type_details, container, false);

        // Getting index of current client from previous fragment
        Bundle args = getArguments();
        assert args != null;
        int appointmentTypeIndex = Integer.parseInt(args.getString("appointmentTypeIndex"));
        currAppointmentType = DatabaseUtils.getAppointmentTypes().get(appointmentTypeIndex);

        requireActivity().setTitle("Edit Appointment Type");

        EnterAppointmentTypeDetails enterAppointmentTypeDetails = new EnterAppointmentTypeDetails(view, currAppointmentType);

        enterAppointmentTypeDetails.getSaveButton().setOnClickListener(v -> {
            String typeName = enterAppointmentTypeDetails.getTypeName().getText().toString();
            String price = enterAppointmentTypeDetails.getPrice().getText().toString();
            String duration = enterAppointmentTypeDetails.getDuration().getText().toString();
            String uid = currAppointmentType.getUid();
            DatabaseUtils.addAppointmentTypeToDatabase(new AppointmentType(typeName, price, duration, uid));

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppointmentTypesMainFragment()).commit();
        });

        enterAppointmentTypeDetails.getDeleteButton().setOnClickListener(v -> {
            // Ask for confirmation before deleting the client
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Deletion of Appointment Type");
            builder.setMessage("Are you sure you want to delete " + currAppointmentType.getTypeName() + "?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) { // Confirmation received
                    DatabaseUtils.deleteAppointmentTypeFromDatabase(currAppointmentType); // Delete Appointment type from database
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppointmentTypesMainFragment()).commit(); // Go back to previous fragment
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return view;
    }
}
