package com.example.abm_improved.Appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.Appointments.Templates.EnterAppointmentDetails;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DateUtils;
import com.example.abm_improved.Utils.PopupTimePicker;

import java.util.Objects;
import java.util.UUID;

public class AddNewAppointmentFragment extends BasePopupDialog {

    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";

    private static final String TAG = "AddNewAppointmentFragment";

    private EnterAppointmentDetails enterAppointmentDetails;

    public static AddNewAppointmentFragment newInstance(int year, int month, int day) {
        AddNewAppointmentFragment fragment = new AddNewAppointmentFragment();

        // Pass the data to the DialogFragment
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);

        return fragment;
    }

    public static AddNewAppointmentFragment newInstance(int year, int month, int day, int hour, int minute) {
        AddNewAppointmentFragment fragment = new AddNewAppointmentFragment();

        // Pass the data to the DialogFragment
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_HOUR, hour);
        args.putInt(ARG_MINUTE, minute);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_details, container, false);

        assert getArguments() != null;
        Bundle bundle = getArguments();
        int year = bundle.getInt(ARG_YEAR);
        int month = bundle.getInt(ARG_MONTH);
        int day = bundle.getInt(ARG_DAY);
        int hour = bundle.getInt(ARG_HOUR, 9); //default time is 09:00 in the morning //todo: make this a setting
        int minute = bundle.getInt(ARG_MINUTE, 0);

        enterAppointmentDetails = new EnterAppointmentDetails(view, requireActivity(), EnterAppointmentDetails.ADDING_APPOINTMENT, null);

        enterAppointmentDetails.getDialogCloseButton().setOnClickListener(v -> dismiss()); // When the close button is clicked, dismiss (close) the dialog

        if (year != -1 && month != -1 && day != -1) {
            enterAppointmentDetails.getAppointmentDateTextView().setText(DateUtils.makeDateString(day, month, year));
        }
        if (hour != -1 && minute != -1) {
            int roundedMinutes = minute / 10 * 10; // Rounding minutes to nearest 10 minutes
            enterAppointmentDetails.getAppointmentTimeTextView().setText(PopupTimePicker.getTimeString(hour, roundedMinutes));
        }

        enterAppointmentDetails.getAddAppointmentButton().setOnClickListener(v -> {
            if (enterAppointmentDetails.getAppointmentTypeIndexChosen() == -1 || enterAppointmentDetails.getClientIndexChosen() == -1) {
                Toast.makeText(requireActivity(), "Please select a client and the type of appointment", Toast.LENGTH_SHORT).show();
            } else {
                String appointmentType = DatabaseUtils.getAppointmentTypes().get(enterAppointmentDetails.getAppointmentTypeIndexChosen()).getUid();
                String appointmentClient = DatabaseUtils.getClients().get(enterAppointmentDetails.getClientIndexChosen()).getUid();
                String appointmentDate = DateUtils.stringToInt(enterAppointmentDetails.getAppointmentDateTextView().getText().toString()) + "";
                String appointmentTime = PopupTimePicker.reformatToBasicString(enterAppointmentDetails.getAppointmentTimeTextView().getText().toString());
                String uid = UUID.randomUUID().toString();

                Appointment appointment = new Appointment(uid, appointmentClient, appointmentType, appointmentDate, appointmentTime);
                DatabaseUtils.addAppointmentToDatabase(appointment);

                dismiss(); // Dismiss (close) the dialog
            }
        });

        return view;
    }

}
