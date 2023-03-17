package com.example.abm_improved.AppointmentTypes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abm_improved.AppointmentTypes.Templates.EnterAppointmentTypeDetails;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.AppointmentType;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;

import java.util.UUID;

public class AddNewAppointmentTypeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_enter_appointment_type_details, container, false);

        EnterAppointmentTypeDetails enterAppointmentTypeDetails = new EnterAppointmentTypeDetails(view);

        requireActivity().setTitle("Add New Appointment Type");

        enterAppointmentTypeDetails.getSaveButton().setOnClickListener(v -> {
            String typeName = enterAppointmentTypeDetails.getTypeName().getText().toString();
            String price = enterAppointmentTypeDetails.getPrice().getText().toString();
            String duration = enterAppointmentTypeDetails.getDuration().getText().toString();
            String uid = UUID.randomUUID().toString();

            if (typeName.isEmpty() || price.isEmpty() || duration.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill All required fields!", Toast.LENGTH_SHORT).show();
            }
            DatabaseUtils.addAppointmentTypeToDatabase(new AppointmentType(typeName, price, duration, uid));

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppointmentTypesMainFragment()).commit();
        });

        return view;
    }
}
