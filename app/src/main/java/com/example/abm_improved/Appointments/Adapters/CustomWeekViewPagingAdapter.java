package com.example.abm_improved.Appointments.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alamkanak.weekview.WeekViewEntity;
import com.alamkanak.weekview.WeekView;
import com.example.abm_improved.Appointments.AddNewAppointmentFragment;
import com.example.abm_improved.Appointments.EditAppointmentFragment;
import com.example.abm_improved.Appointments.Templates.MyWeekView;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.Utils.DateUtils;

import java.util.Calendar;

public class CustomWeekViewPagingAdapter extends WeekView.PagingAdapter<Appointment> {

    FragmentActivity currActivity;

    public CustomWeekViewPagingAdapter(FragmentActivity currActivity) {
        this.currActivity = currActivity;
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

    // Loads more events and updates WeekView
    @Override
    public void onLoadMore(@NonNull Calendar startDate, @NonNull Calendar endDate) {
        int startDateInt = DateUtils.calendarToInt(startDate);
        int endDateInt = DateUtils.calendarToInt(endDate);

        submitList(MyWeekView.getAppointmentsBetweenDates(startDateInt, endDateInt));
    }

    @Override
    public void onEmptyViewClick(@NonNull Calendar time) {
        super.onEmptyViewClick(time);

        int year = time.get(Calendar.YEAR);     // get the year
        int month = time.get(Calendar.MONTH) + 1;   // get the month
        int day = time.get(Calendar.DAY_OF_MONTH); // get the day of the month
        int hour = time.get(Calendar.HOUR_OF_DAY); // get the hour of the day
        int minute = time.get(Calendar.MINUTE); // get the minute of the day
        AddNewAppointmentFragment dialogFragment = AddNewAppointmentFragment.newInstance(year, month, day, hour, minute);
        dialogFragment.show(currActivity.getSupportFragmentManager(), "AddNewAppointmentFragment"); // open popup window
    }

    @Override
    public void onEmptyViewLongClick(@NonNull Calendar time) { // todo possibly implement
        super.onEmptyViewLongClick(time);
    }

    @Override
    public void onEventClick(Appointment data) {
        super.onEventClick(data);

        EditAppointmentFragment dialogFragment = EditAppointmentFragment.newInstance(data.getUid());
        dialogFragment.show(currActivity.getSupportFragmentManager(), "AddNewAppointmentFragment"); // open popup window
    }

    @Override
    public void onEventLongClick(Appointment data) {
        super.onEventLongClick(data);
    }
}
