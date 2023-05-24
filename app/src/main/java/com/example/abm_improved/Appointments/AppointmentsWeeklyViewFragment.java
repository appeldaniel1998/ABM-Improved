package com.example.abm_improved.Appointments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.example.abm_improved.Appointments.Adapters.CustomWeekViewPagingAdapter;
import com.example.abm_improved.Appointments.Templates.MyWeekView;
import com.example.abm_improved.BaseFragment;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.Interfaces;
import com.example.abm_improved.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This <a href="https://github.com/thellmund/Android-Week-View">library</a> was used to create the week view
 */
public class AppointmentsWeeklyViewFragment extends BaseFragment {

    private MyWeekView myWeekView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments_weekly_3days_daily_view, container, false);

        myWeekView = new MyWeekView(view, MyWeekView.WEEKLY_CALENDAR, requireActivity());

        return view;
    }
}