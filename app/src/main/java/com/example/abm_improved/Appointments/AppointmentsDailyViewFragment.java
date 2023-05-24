package com.example.abm_improved.Appointments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abm_improved.Appointments.Templates.MyWeekView;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.R;

public class AppointmentsDailyViewFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_weekly_3days_daily_view, container, false);

        MyWeekView myWeekView = new MyWeekView(view, MyWeekView.ONE_DAY_CALENDAR, requireActivity());

        return view;
    }
}