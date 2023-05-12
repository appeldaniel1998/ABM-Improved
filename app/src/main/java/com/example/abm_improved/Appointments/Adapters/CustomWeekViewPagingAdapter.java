package com.example.abm_improved.Appointments.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEntity;
import com.alamkanak.weekview.PublicApi;
import com.alamkanak.weekview.WeekView;
import com.example.abm_improved.DataClasses.Appointment;

import java.util.Calendar;

public class CustomWeekViewPagingAdapter extends WeekView.PagingAdapter<Appointment> {

    public CustomWeekViewPagingAdapter() {
    }


    @NonNull
    @Override
    public WeekViewEntity onCreateEntity(@NonNull Appointment item) {
        return new WeekViewEntity.Event.Builder(item)
                .setId(item.getUidAsLong()) // id of item (long)
                .setTitle(item.getClientUid()) // title to display (String) //todo change to proper title
                .setStartTime(item.getStartTime()) // start time (Calendar object)
                .setEndTime(item.getEndTime()) // end time (Calendar object)
                .build();
    }

    @Override
    public void onLoadMore(@NonNull Calendar startDate, @NonNull Calendar endDate) {
        // Load more events and update WeekView here
    }
}
