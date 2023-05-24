package com.example.abm_improved.Appointments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.example.abm_improved.Appointments.Adapters.CustomWeekViewPagingAdapter;
import com.example.abm_improved.Appointments.Templates.MyWeekView;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DateUtils;
import com.example.abm_improved.Utils.Interfaces;

import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentsThreeDaysViewFragment extends Fragment {

    private WeekView weekView;
    private CustomWeekViewPagingAdapter weekViewPagingAdapter;
    private int[] currentDateRange; // The current date range being displayed on the week view (3 weeks before and after)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments_weekly_3days_daily_view, container, false);

        MyWeekView myWeekView = new MyWeekView(view, MyWeekView.THREE_DAYS_CALENDAR, requireActivity());

        return view;
    }
}