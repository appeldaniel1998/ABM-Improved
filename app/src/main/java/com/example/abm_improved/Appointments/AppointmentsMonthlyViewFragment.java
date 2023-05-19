package com.example.abm_improved.Appointments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DateUtils;

public class AppointmentsMonthlyViewFragment extends BaseFragment {

    private DatePicker calendar;
    private Button addAppointmentButton;

    private NavController navController;

    private int year;
    private int month;
    private int day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_main, container, false);

        navController = NavHostFragment.findNavController(AppointmentsMonthlyViewFragment.this);

        calendar = view.findViewById(R.id.calendar);
        addAppointmentButton = view.findViewById(R.id.add_appointment_button);

        int[] todayDate = DateUtils.getTodayDateAsInts(); // get today's date as ints (3 ints in the array)
        year = todayDate[0];
        month = todayDate[1];
        day = todayDate[2];

        calendar.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
            this.year = year;
            this.month = monthOfYear + 1;
            this.day = dayOfMonth;
        });

        addAppointmentButton.setOnClickListener(v -> {
            AddNewAppointmentFragment dialogFragment = AddNewAppointmentFragment.newInstance(year, month, day);
            dialogFragment.show(requireActivity().getSupportFragmentManager(), "AddNewAppointmentFragment");
        });

        return view;
    }
}