package com.example.abm_improved.Appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.Appointments.Templates.EnterAppointmentDetails;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DateUtils;
import com.example.abm_improved.Utils.PopupTimePicker;

public class EditAppointmentFragment extends BasePopupDialog {

    private static final String ARG_APPOINTMENT_INDEX = "appointmentIndex";
    private static final String ARG_APPOINTMENT_UID = "appointmentUid";

    private Appointment currAppointment;

    public static EditAppointmentFragment newInstance(int position) {
        EditAppointmentFragment fragment = new EditAppointmentFragment();

        // Pass the data to the DialogFragment
        Bundle args = new Bundle();
        args.putInt(ARG_APPOINTMENT_INDEX, position);
        fragment.setArguments(args);

        return fragment;
    }

    public static EditAppointmentFragment newInstance(String appointmentUid) {
        EditAppointmentFragment fragment = new EditAppointmentFragment();

        // Pass the data to the DialogFragment
        Bundle args = new Bundle();
        args.putString(ARG_APPOINTMENT_UID, appointmentUid);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_details, container, false);


        // Getting index of current client from previous fragment
        assert getArguments() != null;
        Bundle bundle = getArguments();
        int appointmentIndex = bundle.getInt(ARG_APPOINTMENT_INDEX, -1);
        if (appointmentIndex != -1) {
            currAppointment = DatabaseUtils.getAppointments().get(appointmentIndex);
        } else {
            String currAppointmentUid = bundle.getString(ARG_APPOINTMENT_UID);
            currAppointment = DatabaseUtils.getAppointmentByUid(currAppointmentUid);
        }

        EnterAppointmentDetails enterAppointmentDetails = new EnterAppointmentDetails(view, requireActivity(), EnterAppointmentDetails.EDITING_APPOINTMENT, currAppointment);
        enterAppointmentDetails.getDialogCloseButton().setOnClickListener(v -> dismiss()); // When the close button is clicked, dismiss (close) the dialog

        Button deleteAppointmentButton = enterAppointmentDetails.getDeleteAppointmentButton();
        deleteAppointmentButton.setVisibility(View.VISIBLE);
        deleteAppointmentButton.setOnClickListener(v -> {
            DatabaseUtils.deleteAppointmentFromDatabase(currAppointment);
            dismiss(); // dismiss the dialog
        });

        enterAppointmentDetails.getAddAppointmentButton().setOnClickListener(v -> {
            String appointmentType = DatabaseUtils.getAppointmentTypes().get(enterAppointmentDetails.getAppointmentTypeIndexChosen()).getUid();
            String appointmentClient = DatabaseUtils.getClients().get(enterAppointmentDetails.getClientIndexChosen()).getUid();
            String appointmentDate = DateUtils.stringToInt(enterAppointmentDetails.getAppointmentDateTextView().getText().toString()) + "";
            String appointmentTime = PopupTimePicker.reformatToBasicString(enterAppointmentDetails.getAppointmentTimeTextView().getText().toString());
            String uid = currAppointment.getUid();

            Appointment appointment = new Appointment(uid, appointmentClient, appointmentType, appointmentDate, appointmentTime);
            DatabaseUtils.addAppointmentToDatabase(appointment);
            Toast.makeText(requireContext(), "Added Successfully!", Toast.LENGTH_SHORT).show();

            dismiss(); // dismiss the dialog
        });

        return view;
    }
}
