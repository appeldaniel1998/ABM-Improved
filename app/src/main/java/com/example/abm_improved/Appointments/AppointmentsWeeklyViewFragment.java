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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentsWeeklyViewFragment extends BaseFragment {

    private WeekView weekView;
    private CustomWeekViewPagingAdapter weekViewPagingAdapter;
    private int[] currentDateRange; // The current date range being displayed on the week view (3 weeks before and after)
    private ArrayList<Appointment> inRangeAppointments; // The appointments that are in the current date range

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments_weekly_view, container, false);

        weekView = view.findViewById(R.id.weekView);
        weekViewPagingAdapter = new CustomWeekViewPagingAdapter();
        weekView.setAdapter(weekViewPagingAdapter); // Set the adapter for the week view

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
        weekView.setOnTouchListener((v, event) -> setNewHorizontalScrolling().onTouchEvent(event)); // Set custom horizontal scrolling

        DatabaseUtils.getAllAppointmentTypesFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllClientsFromDatabase(new Interfaces.DoNothing());
        DatabaseUtils.getAllAppointmentsFromDatabase(new AppointmentsWeeklyViewFragment.OnGetAllAppointments());

        currentDateRange = get3WeeksBeforeAndAfterDates(DateUtils.getTodayDateAsInt());

        return view;
    }

    /**
     * Creates a new GestureDetector that overrides the WeekView's default horizontal scrolling.
     * Instead of scrolling by flinging, the view will only scroll horizontally when a swipe gesture is detected.
     *
     * A swipe gesture is considered to be "more" horizontal than vertical, and is either a swipe to the left (next week)
     * or a swipe to the right (previous week). The threshold for a swipe is determined by the 'swipeThreshold' field.
     *
     * @return A GestureDetector with the custom onFling method for the WeekView.
     */
    private GestureDetector setNewHorizontalScrolling()
    {
        int swipeThreshold = 100; // The minimum distance the user has to swipe to trigger a swipe gesture
        return new GestureDetector(requireActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) { // If the swipe is "more" horizontal than vertical
                    Calendar cal = weekView.getFirstVisibleDate();
                    if (e1.getX() - e2.getX() > swipeThreshold) { // Swipe left (next week)
                        cal.add(Calendar.DATE, 7);
                        weekView.scrollToDate(cal);
                        return true;
                    } else if (e2.getX() - e1.getX() > swipeThreshold) { // Swipe right (previous week)
                        cal.add(Calendar.DATE, -7);
                        weekView.scrollToDate(cal);
                        return true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * This method calculates the first day (Sunday) and the last day (Saturday) of the week,
     * respectively 3 weeks prior and 3 weeks after a given date.
     *
     * <p>
     * The input is an integer in the yyyymmdd format. The method returns an array of two integers,
     * where the first integer is the first day of the week from 3 weeks prior to the given date,
     * and the second integer is the last day of the week from 3 weeks after the given date.
     * Both the returned dates are also in the yyyymmdd format.
     * </p>
     *
     * <p>
     * Note: This method assumes that the first day of the week is Sunday and the last day is Saturday.
     * Adjustments may be needed based on the locale.
     * </p>
     *
     * @param date the date as an integer in the format of yyyymmdd
     * @return an array of two integers, where the first integer is the date of the first day
     * of the week from 3 weeks prior to the given date, and the second integer is
     * the date of the last day of the week from 3 weeks after the given date.
     * Both dates are in the yyyymmdd format.
     * @throws DateTimeParseException if the input date cannot be parsed
     */
    public int[] get3WeeksBeforeAndAfterDates(int date) {
        // Convert the date into a string
        String dateString = Integer.toString(date);

        // Parse the date string into a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Get the first day of the week, 3 weeks prior
        LocalDate startDate = localDate.minusWeeks(3)
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));

        // Get the last day of the week, 3 weeks after
        LocalDate endDate = localDate.plusWeeks(3)
                .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY));

        // Convert the dates back to integers
        int startInt = Integer.parseInt(startDate.format(formatter));
        int endInt = Integer.parseInt(endDate.format(formatter));

        return new int[]{startInt, endInt};
    }

    private class OnGetAllAppointments implements Interfaces.OnFinishQueryInterface {
        @Override
        public void onFinishQuery() {
            //todo init recycler views
            inRangeAppointments = new ArrayList<>();
            ArrayList<Appointment> appointments = DatabaseUtils.getAppointments();
            for (Appointment appointment : appointments) {
                int appointmentDate = Integer.parseInt(appointment.getDate());
                if (appointmentDate >= currentDateRange[0] && appointmentDate <= currentDateRange[1]) {
                    appointment.getStartEndTime();
                    inRangeAppointments.add(appointment);
                }
            }
            weekViewPagingAdapter.submitList(inRangeAppointments);

        }
    }
}