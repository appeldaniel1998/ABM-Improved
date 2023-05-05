package com.example.abm_improved.Appointments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.AppointmentTypes.AddNewAppointmentTypeFragment;
import com.example.abm_improved.Appointments.Templates.EnterAppointmentDetails;
import com.example.abm_improved.BaseActivity;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.LoginAndRegister.LoginFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.PopupDatePicker;
import com.example.abm_improved.Utils.PopupTimePicker;

import java.util.UUID;

public class AddNewAppointmentFragment extends BaseFragment {

    private static final String TAG = "AddNewAppointmentFragment";

    private EnterAppointmentDetails enterAppointmentDetails;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_details, container, false);

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new Interfaces.DoNothing());

        navController = NavHostFragment.findNavController(AddNewAppointmentFragment.this);


        assert getArguments() != null;
        AddNewAppointmentFragmentArgs args = AddNewAppointmentFragmentArgs.fromBundle(getArguments());
        int year = args.getYear();
        int month = args.getMonth();
        int day = args.getDay();

        enterAppointmentDetails = new EnterAppointmentDetails(view, requireActivity(), EnterAppointmentDetails.ADDING_APPOINTMENT, null);

        if (year != -1 && month != -1 && day != -1) {
            enterAppointmentDetails.getAppointmentDateTextView().setText(PopupDatePicker.makeDateString(day, month, year));
        }

        enterAppointmentDetails.getAddAppointmentButton().setOnClickListener(v -> {
            if (enterAppointmentDetails.getAppointmentTypeIndexChosen() == -1 || enterAppointmentDetails.getClientIndexChosen() == -1) {
                Toast.makeText(requireActivity(), "Please select a client and the type of appointment", Toast.LENGTH_SHORT).show();
            } else {
                String appointmentType = DatabaseUtils.getAppointmentTypes().get(enterAppointmentDetails.getAppointmentTypeIndexChosen()).getUid();
                String appointmentClient = DatabaseUtils.getClients().get(enterAppointmentDetails.getClientIndexChosen()).getUid();
                String appointmentDate = PopupDatePicker.stringToInt(enterAppointmentDetails.getAppointmentDateTextView().getText().toString()) + "";
                String appointmentTime = PopupTimePicker.StringToInt(enterAppointmentDetails.getAppointmentTimeTextView().getText().toString()) + "";
                String uid = UUID.randomUUID().toString();

                Appointment appointment = new Appointment(uid, appointmentClient, appointmentType, appointmentDate, appointmentTime);
                DatabaseUtils.addAppointmentToDatabase(appointment);

                navController.popBackStack();
            }
        });

        return view;
    }

}
