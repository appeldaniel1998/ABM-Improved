package com.example.abm_improved.Appointments.Templates;

import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.alamkanak.weekview.WeekView;
import com.example.abm_improved.Appointments.Adapters.CustomWeekViewPagingAdapter;
import com.example.abm_improved.DataClasses.Appointment;
import com.example.abm_improved.R;
import com.example.abm_improved.Utils.DatabaseUtils;
import com.example.abm_improved.Utils.DateUtils;
import com.example.abm_improved.Utils.Interfaces;

import java.util.ArrayList;
import java.util.Calendar;

public class MyWeekView {
    public static final int WEEKLY_CALENDAR = 7;
    public static final int THREE_DAYS_CALENDAR = 3;
    public static final int ONE_DAY_CALENDAR = 1;

    private final int FLUID_SCROLLING = 0;
    private final int STATIC_SCROLLING = 1;
    private int fluidOrStaticScrolling = STATIC_SCROLLING; // 0 = fluid scrolling, 1 = static scrolling
    private WeekView weekView;
    private CustomWeekViewPagingAdapter weekViewPagingAdapter;
    private FragmentActivity currActivity;
    private int[] currentDateRange; // The current date range being displayed on the week view (3 weeks before and after)
    private int currNumberOfDays;

    public MyWeekView(View view, int numOfDays, FragmentActivity currActivity) {
        this.currActivity = currActivity;
        this.currNumberOfDays = numOfDays;
        weekView = view.findViewById(R.id.weekView);
        weekViewPagingAdapter = new CustomWeekViewPagingAdapter(this.currActivity);
        weekView.setAdapter(weekViewPagingAdapter); // Set the adapter for the week view

        // header styling ------------------------------------------------------>
        weekView.setShowHeaderBottomLine(true); // Show the line at the bottom of the header
        weekView.setShowHeaderBottomShadow(true);
        // <-------------------------------------------------------------------

        weekView.setVerticalScrollBarEnabled(true); // Enable vertical scroll bar

        if (numOfDays == WEEKLY_CALENDAR) {
            // scroll to first day (sunday) of the current week ----------------->
            Calendar calendar = Calendar.getInstance(); // Get the current date
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) { // Check if today is not Sunday
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) { // If it's not Sunday, find the previous Sunday
                    calendar.add(Calendar.DATE, -1);
                }
            }
            weekView.scrollToDate(calendar); // Set the first visible date as the found Sunday
            // <-------------------------------------------------------------------
        }
        else if (numOfDays == THREE_DAYS_CALENDAR) {
            weekView.setNumberOfVisibleDays(3);
        }
        else {
            weekView.setNumberOfVisibleDays(1);
        }

        if (fluidOrStaticScrolling == STATIC_SCROLLING) { // If static scrolling is enabled                 // disabled by default. todo: make this a setting
            // todo: implement smooth scrolling without partially scrolled week
            weekView.setHorizontalScrollingEnabled(false); // Disable default horizontal fling/scroll
            setNewHorizontalScrolling(); // Set new horizontal scrolling
        }

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllClientsFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllAppointmentsFromDatabase(new OnGetAllAppointments());

        currentDateRange = DateUtils.get3WeeksBeforeAndAfterDates(DateUtils.getTodayDateAsInt());
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
        GestureDetector gestureDetector = new GestureDetector(currActivity, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) { // If the swipe is "more" horizontal than vertical
                    Calendar cal = weekView.getFirstVisibleDate();
                    if (e1.getX() - e2.getX() > 150) { // Swipe left (next week)
                        cal.add(Calendar.DATE, currNumberOfDays);
                        weekView.scrollToDate(cal);
                    } else if (e2.getX() - e1.getX() > 150) { // Swipe right (previous week)
                        cal.add(Calendar.DATE, -currNumberOfDays);
                        weekView.scrollToDate(cal);
                    }

                    return true;
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
            weekViewPagingAdapter.submitList(getAppointmentsBetweenDates(currentDateRange[0], currentDateRange[1]));
        }
    }

    /**
     * Returns an ArrayList of appointments that are between the given start and end dates.
     * Assumption: The start and end dates are in the format YYYYMMDD.
     * Assumption: The appointments are loaded from the database.
     *
     * @param startDate The start date of the date range
     * @param endDate   The end date of the date range
     * @return An ArrayList of appointments that are between the given start and end dates
     */
    public static ArrayList<Appointment> getAppointmentsBetweenDates(int startDate, int endDate) {
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
