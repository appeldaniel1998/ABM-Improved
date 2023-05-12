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

    private WeekView weekView;
    private CustomWeekViewPagingAdapter weekViewPagingAdapter;
    private int[] currentDateRange; // The current date range being displayed on the week view (3 weeks before and after)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments_weekly_view, container, false);

        weekView = view.findViewById(R.id.weekView);
        weekViewPagingAdapter = new CustomWeekViewPagingAdapter();
        weekView.setAdapter(weekViewPagingAdapter); // Set the adapter for the week view

        // header styling ------------------------------------------------------>
        weekView.setShowHeaderBottomLine(true); // Show the line at the bottom of the header
        weekView.setShowHeaderBottomShadow(true);
        // <-------------------------------------------------------------------

        // scroll to first day (sunday) of the current week ----------------->
        Calendar calendar = Calendar.getInstance(); // Get the current date
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) { // Check if today is not Sunday
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) { // If it's not Sunday, find the previous Sunday
                calendar.add(Calendar.DATE, -1);
            }
        }
        weekView.scrollToDate(calendar); // Set the first visible date as the found Sunday
        // <-------------------------------------------------------------------


        weekView.setHorizontalScrollingEnabled(false); // Disable default horizontal fling/scroll
        setNewHorizontalScrolling(); // Set new horizontal scrolling

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllClientsFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllAppointmentsFromDatabase(new AppointmentsWeeklyViewFragment.OnGetAllAppointments());

        currentDateRange = DateUtils.get3WeeksBeforeAndAfterDates(DateUtils.getTodayDateAsInt());

        return view;
    }

    /**
     * Creates a new GestureDetector that overrides the WeekView's default horizontal scrolling.
     * Instead of scrolling by flinging, the view will only scroll horizontally when a swipe gesture is detected.
     * <p>
     * A swipe gesture is considered to be "more" horizontal than vertical, and is either a swipe to the left (next week)
     * or a swipe to the right (previous week). The threshold for a swipe is determined by the 'swipeThreshold' field.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setNewHorizontalScrolling() {
        GestureDetector gestureDetector = new GestureDetector(requireActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) { // If the swipe is "more" horizontal than vertical
                    Calendar cal = weekView.getFirstVisibleDate();
                    if (e1.getX() - e2.getX() > 150) { // Swipe left (next week)
                        cal.add(Calendar.DATE, 7);
                        weekView.scrollToDate(cal);
                        return true;
                    } else if (e2.getX() - e1.getX() > 150) { // Swipe right (previous week)
                        cal.add(Calendar.DATE, -7);
                        weekView.scrollToDate(cal);
                        return true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        weekView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event)); // Set custom horizontal scrolling
    }

    private class OnGetAllAppointments implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            // The appointments that are in the current date range are added to the week view
            weekViewPagingAdapter.submitList(getAppointmentsBetweenDated(currentDateRange[0], currentDateRange[1]));

        }
    }

    public static ArrayList<Appointment> getAppointmentsBetweenDated(int startDate, int endDate) {
        ArrayList<Appointment> inRangeAppointments = new ArrayList<>();
        for (Appointment appointment : DatabaseUtils.getAppointments()) {
            int appointmentDate = Integer.parseInt(appointment.getDate());
            if (appointmentDate >= startDate && appointmentDate <= endDate) {
                appointment.setStartEndTime(); // Set the start and end time of the appointment
                inRangeAppointments.add(appointment);   // Add the appointment to the list if it's in the date range
            }
        }
        return inRangeAppointments;
    }
}