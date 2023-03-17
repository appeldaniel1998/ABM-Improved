package com.example.abm_improved.AppointmentTypes.Templates;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.abm_improved.DataClasses.AppointmentType;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;

import java.util.UUID;

public class EnterAppointmentTypeDetails {
    private EditText typeName;
    private EditText price;
    private EditText duration;
    private Button saveButton;
    private Button deleteButton;

    public EnterAppointmentTypeDetails(View view) { // Creating a new appointment type
        this.findXmlFields(view);
    }

    public EnterAppointmentTypeDetails(View view, AppointmentType appointmentType) { // Editing an existing appointment type
        this.findXmlFields(view);
        this.typeName.setText(appointmentType.getTypeName());
        this.price.setText(appointmentType.getPrice());
        this.duration.setText(appointmentType.getDuration());

        this.deleteButton.setVisibility(View.VISIBLE);
    }

    private void findXmlFields(View view) {
        this.typeName = view.findViewById(R.id.typeNameEditText);
        this.price = view.findViewById(R.id.priceEditText);
        this.duration = view.findViewById(R.id.durationEditText);
        this.saveButton = view.findViewById(R.id.doneEditingButton);
        this.deleteButton = view.findViewById(R.id.deleteButton);
    }

    public EditText getTypeName() {
        return typeName;
    }

    public EditText getPrice() {
        return price;
    }

    public EditText getDuration() {
        return duration;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}
