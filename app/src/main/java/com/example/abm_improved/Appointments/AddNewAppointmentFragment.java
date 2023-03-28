package com.example.abm_improved.Appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.Appointments.Templates.EnterAppointmentDetails;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.PopupDatePicker;
import com.example.abm_improved.Utils.PopupTimePicker;

import java.util.UUID;

public class AddNewAppointmentFragment extends BaseFragment {

    private EnterAppointmentDetails enterAppointmentDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_details, container, false);

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new Interfaces.DoNothing());

        Bundle args = getArguments();
        assert args != null;
        int year = -1;
        int month = -1;
        int day = -1;
        if (!args.getString("year").equals("")) {
            year = Integer.parseInt(args.getString("year"));
        }
        if (!args.getString("month").equals("")) {
            month = Integer.parseInt(args.getString("month"));
        }
        if (!args.getString("day").equals("")) {
            day = Integer.parseInt(args.getString("day"));
        }

        enterAppointmentDetails = new EnterAppointmentDetails(view, requireActivity(), EnterAppointmentDetails.ADDING_APPOINTMENT, null);

        if (year != -1 && month != -1 && day != -1) {
            enterAppointmentDetails.getAppointmentDateTextView().setText(PopupDatePicker.makeDateString(day, month, year));
        }

        enterAppointmentDetails.getAddAppointmentButton().setOnClickListener(v -> {
            String appointmentType = DatabaseUtils.getAppointmentTypes().get(enterAppointmentDetails.getAppointmentTypeIndexChosen()).getUid();
            String appointmentClient = DatabaseUtils.getClients().get(enterAppointmentDetails.getClientIndexChosen()).getUid();
            String appointmentDate = PopupDatePicker.stringToInt(enterAppointmentDetails.getAppointmentDateTextView().getText().toString()) + "";
            String appointmentTime = PopupTimePicker.StringToInt(enterAppointmentDetails.getAppointmentTimeTextView().getText().toString()) + "";
            String uid = UUID.randomUUID().toString();

            Appointment appointment = new Appointment(uid, appointmentClient, appointmentType, appointmentDate, appointmentTime);
            DatabaseUtils.addAppointmentToDatabase(appointment);
        });

        return view;
    }

}
