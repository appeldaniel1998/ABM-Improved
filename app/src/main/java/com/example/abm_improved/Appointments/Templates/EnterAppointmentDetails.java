package com.example.abm_improved.Appointments.Templates;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.abm_improved.DataClasses.AppointmentType;
import com.example.abm_improved.DataClasses.Client;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.PopupDatePicker;
import com.example.abm_improved.Utils.PopupTimePicker;

import java.util.ArrayList;

public class EnterAppointmentDetails {

    private final AutoCompleteTextView appointmentTypeEditText;
    private final AutoCompleteTextView clientNameEditText;
    private final LinearLayout timeLinearLayout;
    private final TextView appointmentTimeTextView;
    private final LinearLayout dateLinearLayout;
    private final TextView appointmentDateTextView;
    private final Button addAppointmentButton;
    private final Button deleteAppointmentButton;

    private final DatePickerDialog datePickerDialog; //for date picker

    private PopupTimePicker timePicker;

    private final FragmentActivity currentActivity;

    private int clientIndexChosen = -1;
    private int appointmentTypeIndexChosen = -1;

    public EnterAppointmentDetails(View view, FragmentActivity activity) {
        this.appointmentTypeEditText = view.findViewById(R.id.appointmentTypeEditText);
        this.clientNameEditText = view.findViewById(R.id.clientNameEditText);
        this.appointmentDateTextView = view.findViewById(R.id.datePlaceholder);
        this.appointmentTimeTextView = view.findViewById(R.id.timePlaceholder);
        this.addAppointmentButton = view.findViewById(R.id.doneButton);
        this.deleteAppointmentButton = view.findViewById(R.id.deleteButton);
        this.timeLinearLayout = view.findViewById(R.id.timeLinearLayout);
        this.dateLinearLayout = view.findViewById(R.id.dateLinearLayout);
        this.currentActivity = activity;

        this.datePickerDialog = PopupDatePicker.initDatePicker(appointmentDateTextView, activity);
        this.appointmentDateTextView.setText(PopupDatePicker.getTodayDate()); // Set initial date to today's date
        this.dateLinearLayout.setOnClickListener(v -> datePickerDialog.show()); //onclick listener for birthdayLinearLayout to choose a date

        this.timeLinearLayout.setOnClickListener(v -> {
            timePicker = new PopupTimePicker(activity, appointmentTimeTextView);
        });
        PopupTimePicker.setCurrentTime(appointmentTimeTextView); // Set initial time to current time

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new OnGetAllAppointmentTypes()); // Get all appointment types from the database and set the drop down menu (in OnGetAllAppointmentTypes)
        DatabaseUtils.getAllClientsFromDatabase(new OnGetAllClients()); // Get all client names from the database and set the drop down menu (in OnGetAllClients)
    }

    public Button getAddAppointmentButton() {
        return addAppointmentButton;
    }

    public Button getDeleteAppointmentButton() {
        return deleteAppointmentButton;
    }

    public EditText getAppointmentTypeEditText() {
        return appointmentTypeEditText;
    }

    public EditText getClientNameEditText() {
        return clientNameEditText;
    }

    public TextView getAppointmentTimeTextView() {
        return appointmentTimeTextView;
    }

    public TextView getAppointmentDateTextView() {
        return appointmentDateTextView;
    }

    public int getClientIndexChosen() {
        return clientIndexChosen;
    }

    public int getAppointmentTypeIndexChosen() {
        return appointmentTypeIndexChosen;
    }

    private class OnGetAllAppointmentTypes implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            ArrayList<String> appointmentTypesAsStrings = new ArrayList<>();
            ArrayList<AppointmentType> appointmentTypes = DatabaseUtils.getAppointmentTypes();
            for (int i = 0; i < appointmentTypes.size(); i++) { // Retrieve all appointment type names as strings from the array list of appointment types (as objects)
                appointmentTypesAsStrings.add(appointmentTypes.get(i).getTypeName());
            }

            appointmentTypeEditText.setAdapter(new ArrayAdapter<>(currentActivity, R.layout.templates_dropdown_menu_list_item, appointmentTypesAsStrings));

            appointmentTypeEditText.setOnItemClickListener((parent, view, position, id) -> appointmentTypeIndexChosen = position);
        }
    }

    private class OnGetAllClients implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            ArrayList<String> clientNamesAsStrings = new ArrayList<>();
            ArrayList<Client> appointmentTypes = DatabaseUtils.getClients();
            for (int i = 0; i < appointmentTypes.size(); i++) { // Retrieve all appointment type names as strings from the array list of appointment types (as objects)
                clientNamesAsStrings.add(appointmentTypes.get(i).getFullName());
            }

            clientNameEditText.setAdapter(new ArrayAdapter<>(currentActivity, R.layout.templates_dropdown_menu_list_item, clientNamesAsStrings));

            clientNameEditText.setOnItemClickListener((parent, view, position, id) -> clientIndexChosen = position);
        }
    }
}
